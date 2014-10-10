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
        Rule(LinkExtractor(allow='.+hagah\.com\.br\/\w+\/\w+\/guia\/$')),

        # Extract links matching company list (category page) and parse them with the spider's method
        # parse_company_list_page
        Rule(LinkExtractor(allow='.+hagah\.com\.br\/\w+\/\w+\/guia\/\w+\?q=.+', canonicalize=False, unique=True),
             callback='parse_company_list_page', follow=True),
    )

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

        #response.xpath('//div[@class="item destacado"]//h2')
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