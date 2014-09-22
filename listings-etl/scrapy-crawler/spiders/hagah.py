import logging
import re
import scrapy

__author__ = 'mikhail'


class HagahSpider(scrapy.Spider):
    name = 'hagah'
    allowed_domains = ['hagah.com.br']
    start_url = [
        'http://www.hagah.com.br/sc/florianopolis/guia/'
    ]

    def __init__(self, name=None, **kwargs):
        self._category_url_pattern = re.compile('.+hagah\.com\.br\/\w+\/\w+\/guia\/')
        self._company_list_url_pattern = re.compile('.+hagah\.com\.br\/\w+\/\w+\/guia\/\w+\?q=.+')
        #the following tuple list is collection of url patterns with corresponding parse functions to apply
        #for the page under matched url
        self._url_pattern_list = [
            ('category_url_pattern', re.compile('.+hagah\.com\.br\/\w+\/\w+\/guia\/'),
             self.parse_category_list),

            ('company_list_url_pattern', re.compile('.+hagah\.com\.br\/\w+\/\w+\/guia\/\w+\?q=.+'),
             self.parse_company_list_page)
        ]
        super(HagahSpider, self).__init__(name, **kwargs)

    def parse(self, response):
        """
        parses multiple hagah pages
        :param response:
        :return:
        """
        parse_func = None
        for e in self._url_pattern_list:
            if e[1].match(response.url):
                logging.info('Matched parsed function %s for url %s' % (e[0], response.url))
                return e[2]

        if parse_func:
            return parse_func(response)
        else:
            logging.error('No parse function was found WTF!? url=%s' % response.url)


    def parse_category_page(self, response):
        """
        parser category list page
        :param response:
        :return:
        """
        pass

    def parse_company_list_page(self, response):
        """
        parser category list page
        :param response:
        :return:
        """
        pass