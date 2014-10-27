# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy

class HighlightedCompany(scrapy.Item):
    """
    class that represents companies that are highlighted in a list - paid companies
    """
    name = scrapy.Field()
    url = scrapy.Field()


class CompanyProfileItem(HighlightedCompany):
    """
    class that holds parsing result of a company profile page
    """
    categories = scrapy.Field()
    address = scrapy.Field()
    url = scrapy.Field()
    phones = scrapy.Field()
    short_description = scrapy.Field()
    full_description = scrapy.Field()
    attributes = scrapy.Field()
    working_hours = scrapy.Field()
    capacity = scrapy.Field()
    credit_cards = scrapy.Field()
    debit_cards = scrapy.Field()
    food_cards = scrapy.Field()
    other_payment_methods = scrapy.Field()
    opened_from = scrapy.Field()
