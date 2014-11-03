# -*- coding: utf-8 -*-
import scrapy

from scrapy.contrib.spiders import Rule, CrawlSpider
from scrapy.contrib.linkextractors import LinkExtractor
from scrapy.shell import inspect_response
from scrapy_crawler import items
from scrapy_crawler.items import *

__author__ = 'mikhail'


class HagahSpider(CrawlSpider):
    name = 'hagah'
    # allowed_domains = ['hagah.com.br']
    start_urls = [
        # 'http://www.hagah.com.br/sc/grande-florianopolis/guia/'
        'http://www.hagah.com.br/sc/florianopolis/local/22541,2,fedoca-by-cuca-restaurante.html'
    ]

    ITEM_PIPELINES = {
        'listings-etl.scrapy_crawler.pipelines.JsonPaidItemPipeline': 100
    }

    rules = (
        # Extract links matching category list page
        # and follow links from them (since no callback means follow=True by default).
        Rule(LinkExtractor(allow='.+hagah\.com\.br\/\w+\/.+\/guia\/$')),

        # Extract links matching company list (category page) and parse them with the spider's method
        # parse_company_list_page
        Rule(LinkExtractor(allow='.+hagah\.com\.br\/\w+\/.+\/guia\/\w+\?q=.+', canonicalize=False, unique=True),
             callback='parse_company_list_page', follow=True),

        # parse company profile page
        Rule(LinkExtractor(allow='.+hagah\.com\.br\/\w+\/.+\/local\/.+\.html$', canonicalize=False, unique=True),
             callback='parse_company_profile_page', follow=False)
    )

    def parse_company_profile_page(self, response):
        """
        parses company profile page and returns company profile items
        :param response:
        :return:
        """

        self.log('Found company profile page url %s' % response.url)
        name = response.css('h1.fn.org::text').extract()
        if name:
            name = name[0]
        else:
            self.log('Error no name found %s' % response.url)
            return

        categories = response.css('div.categoriasDetalhe').css('a::text').extract()
        if categories:
            categories = [c.lower() for c in categories]

        # a link for google maps to get an address
        google_maps_link = response.css('a.btn-map.route').xpath('@href').extract()
        if google_maps_link:
            google_maps_link = google_maps_link[0]

        address = None
        phones = None
        url = None
        for info in response.css('div.infoDetalhe > ul > li'):
            if info.css('.link-endereco').extract():
                # street address
                address = info.css('.link-endereco::text').extract()[0]
                if info.xpath('text()').extract():
                    # number
                    address += ' '
                    address += info.xpath('text()').extract()[0]
                if info.css('.link-bairro').extract():
                    # naeighbourhood
                    address += ' '
                    address += info.css('.link-bairro::text').extract()[0]

            elif info.css('.region').extract():
                if info.xpath('text()').extract():
                    # city
                    address += ' '
                    address += info.xpath('text()').extract()[0]
                address += ' '
                # state
                address += info.css('.region::text').extract()[0]
            elif info.css('.telefones'):
                phones = [p.strip('\t\n') for p in info.xpath('span/text()').extract()]
            elif info.css('.url'):
                url = info.css('.url::text').extract()
                if url:
                    url = url[0]

        short_desc = response.xpath('//*[@id="corpo-abas"]/section/article/h3/text()').extract()
        if short_desc:
            short_desc = short_desc[0]

        full_desc = response.xpath('//*[@id="corpo-abas"]/section/article/p/text()').extract()
        if full_desc:
            full_desc = full_desc[0]

        working_hours = None
        attributes = None
        capacity = None
        credit_card = None
        debit_card = None
        food_card = None
        opened = None
        other_payment_methods = None

        def parse_item(selector):
            item = selector.xpath('span/text()').extract()
            if item and len(item) == 1:
                return item[0]
            else:
                return item

        def parse_list_item(selector):
            return selector.xpath('ul/li/span/text()').extract()

        item_details = response.css('#corpo-abas > section > div.itemInfo')
        if item_details:
            for sel in item_details:
                h3 = sel.xpath('h3/text()').extract()
                if h3:
                    h3 = h3[0].lower()
                    if u'Horário de atendimento'.lower() in h3:
                        working_hours = parse_item(sel)
                    elif u'Capacidade'.lower() in h3:
                        capacity = parse_item(sel)
                    elif u'Características'.lower() in h3:
                        attributes = parse_list_item(sel)
                    elif u'Aceita Cartão de Crédito'.lower() in h3:
                        credit_card = sel.xpath('ul/li/img/@title').extract()
                    elif u'Aceita Cartão de Débito'.lower() in h3:
                        debit_card = sel.xpath('ul/li/img/@title').extract()
                    elif u'Aceita Tíquetes'.lower() in h3:
                        food_card = sel.xpath('ul/li/img/@title').extract()
                    elif u'Mais Formas de Pagamento'.lower() in h3:
                        other_payment_methods = parse_list_item(sel)
                    elif u'Aberto desde' in h3:
                        opened = parse_item(sel)

        def map_utf_list(ll):
            if ll:
                return [field_strip(e.encode('utf-8')) for e in ll]

        def field_strip(field):
            if field:
                return field.strip(' \t\n\r\s')

        return items.CompanyProfileItem(name=field_strip(name), url=field_strip(url),
                                        categories=map_utf_list(categories), address=field_strip(address),
                                        capacity=field_strip(capacity),
                                        phones=map_utf_list(phones), short_description=field_strip(short_desc),
                                        full_description=field_strip(full_desc),
                                        attributes=map_utf_list(attributes),
                                        working_hours=map_utf_list(working_hours),
                                        credit_cards=map_utf_list(credit_card), debit_cards=map_utf_list(debit_card),
                                        food_cards=map_utf_list(food_card),
                                        other_payment_methods=map_utf_list(other_payment_methods),
                                        opened_from=field_strip(opened))

    def parse_company_list_page(self, response):
        """
        parser category list page
        :param response:
        :return:
        """
        self.log('Found company list page url %s' % response.url)
        # we need to construct urls from pages number in element with class=spanResultado
        # if url has page that means that we have already found all pages
        if '&p=' in response.url:
            return

        self.log('Looking for paging on url %s' % response.url)
        # get the number of results to build paging urls
        raw_result_num = response.xpath('//span[@class="spanResultado"]/text()').re('\d+')

        # response.xpath('//div[@class="item destacado"]//h2')
        # CSS equivalent response.css('div[class="item destacado"] h2')
        # to get selected items
        # name: response.css('div.item.destacado h2 a::text').extract()
        # url: response.css('div.item.destacado h2 a').xpath('@href').extract()

        # for n in response.css('div.item.destacado h2 a::text').extract():
        # yield items.HighlightedCompany(name=n)

        if raw_result_num:
            self.log('Got %s-paged paging' % str(raw_result_num[0]))
            result_num = int(raw_result_num[0])
            page_num = int(result_num / 20 + 1)
            for i in range(2, page_num + 1):
                new_url = response.url + '&p=%d' % i
                self.log('Prepared paging url %s' % new_url)
                yield scrapy.Request(url=new_url)

        return