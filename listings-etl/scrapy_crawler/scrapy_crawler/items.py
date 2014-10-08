# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class ScrapyCrawlerItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    pass


class HighlightedCompany(scrapy.Item):
    """
    class that represents companies that are highlighted in a list - paid companies
    """
    name = scrapy.Field()
    url = scrapy.Field()


class CompanyProfileItem(scrapy.Item):
    """
    class that holds parsing result of a company profile page
    """
