# -*- coding: utf-8 -*-

# Scrapy settings for scrapy_crawler project
#
# For simplicity, this file contains only the most important settings by
# default. All the other settings are documented here:
#
#     http://doc.scrapy.org/en/latest/topics/settings.html
#

BOT_NAME = 'scrapy_crawler'

SPIDER_MODULES = ['scrapy_crawler.spiders']
NEWSPIDER_MODULE = 'scrapy_crawler.spiders'

DOWNLOAD_DELAY = 2

# Crawl responsibly by identifying yourself (and your website) on the user-agent
#USER_AGENT = 'scrapy_crawler (+http://www.yourdomain.com)'

########## Item pipeline
# ITEM_PIPELINES = [
#     "scrapy_crawler.pipelines.JsonPaidItemPipeline",
# ]
#
ITEM_PIPELINES = {
    'scrapy_crawler.pipelines.JsonItemPipeline': 800,
}