import logging
import scrapy
from scrapy.contrib.spiders import Rule, CrawlSpider
from scrapy.contrib.linkextractors import LinkExtractor

__author__ = 'mikhail'


class HagahSpider(CrawlSpider):
    name = 'hagah'
    #allowed_domains = ['hagah.com.br']
    start_urls = [
        'http://www.hagah.com.br/sc/florianopolis/guia/'
    ]

    rules = (
        # Extract links matching category list page
        # and follow links from them (since no callback means follow=True by default).
        Rule(LinkExtractor(allow='.+hagah\.com\.br\/\w+\/\w+\/guia\/$')),

        # Extract links matching company list (category page) and parse them with the spider's method
        # parse_company_list_page
        Rule(LinkExtractor(allow='.+hagah\.com\.br\/\w+\/\w+\/guia\/\w+\?q=.+'), callback='parse_company_list_page',
             follow=True),
    )

    def parse_company_list_page(self, response):
        """
        parser category list page
        :param response:
        :return:
        """
        self.log('Found company list page url %s' % response.url)
        # we need to construct urls from pages number in element with class=spanResultado
        #if url has page that means that we have already found all pages
        if '&p=' in response.url:
            return

        self.log('Looking for paging on url %s' % response.url)
        # get the number of results to build paging urls
        raw_result_num = response.xpath('//span[@class="spanResultado"]/text()').re('\d+')
        if raw_result_num:
            result_num = int(raw_result_num[0])
            page_num = int(result_num / 20 + 1)
            for i in range(2, page_num + 1):
                new_url = response.url + '&p=%d' % i
                self.log('Prepared paging url %s' % new_url)
                yield scrapy.Request(url=new_url)

        return