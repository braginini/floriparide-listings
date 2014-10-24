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
        'http://www.hagah.com.br/sc/florianopolis/guia/'
    ]

    ITEM_PIPELINES = {
        'listings-etl.scrapy_crawler.pipelines.JsonPaidItemPipeline': 300
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

        url = response.xpath('//*[@id="miolo"]/div[2]/div[3]/div[1]/div[2]/div[2]/ul[1]/li[3]/a/text()').extract()
        if url:
            url = [2].css('a::text').extract()
            if url:
                url = url[0]

        phones = response.xpath('//*[@id="miolo"]/div[2]/div[4]/div[1]/div[2]/div[3]/ul[1]/li[3]/span/text()').extract()
        if phones:
            #todo remove all /n and /t symbols - clear out phone
            phones = [p for p in phones]

        short_desc = response.xpath('//*[@id="corpo-abas"]/section/article/h3/text()').extract()
        if short_desc:
            short_descr = short_desc[0]

        full_desc = response.xpath('//*[@id="corpo-abas"]/section/article/p/text()').extract()
        if full_desc:
            full_desc = full_desc[0]

        item_infoes = response.css('#corpo-abas > section > div.itemInfo')
        if item_infoes:
            for i in item_infoes:
                h3 = item_infoes.xpath('h3/text()').extract()
                if h3:
                    if 'Horário de atendimento'.lower() in h3[0].lower():
                        working_hours = response.css('#corpo-abas > section > div.itemInfo')[0].xpath(
                            'span/text()').extract()
                        if working_hours:
                            working_hours = working_hours[0]


        attributes = response.css(
            '#corpo-abas > section > div.itemInfo.caract > ul.listaCaract > li > span::text').extract()

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
        #to get selected items
        #name: response.css('div.item.destacado h2 a::text').extract()
        #url: response.css('div.item.destacado h2 a').xpath('@href').extract()

        for n in response.css('div.item.destacado h2 a::text').extract():
            yield items.HighlightedCompany(name=n)

        if raw_result_num:
            self.log('Got %s-paged paging' % str(raw_result_num[0]))
            result_num = int(raw_result_num[0])
            page_num = int(result_num / 20 + 1)
            for i in range(2, page_num + 1):
                new_url = response.url + '&p=%d' % i
                self.log('Prepared paging url %s' % new_url)
                yield scrapy.Request(url=new_url)

        return