# -*- coding: utf-8 -*-
import itertools
import logging

import re

from scrapy.contrib.spiders import Rule, CrawlSpider
from scrapy.contrib.linkextractors import LinkExtractor
from .. import items, utils


class A1188Spider(CrawlSpider):
    name = "1188"
    allowed_domains = ["1188.lv"]
    start_urls = [
        'http://www.1188.lv/catalog/Cafe%2C%20catering',
        'http://www.1188.lv/catalog/Restaurants%2C%20catering',
        'http://www.1188.lv/catalog/Pizzerias%2C%20catering',
        'http://www.1188.lv/catalog/Food%20delivery%2C%20catering',
        'http://www.1188.lv/katalogs/Kafejn%C4%ABca%2C%20%C4%93din%C4%81%C5%A1ana',
        'http://www.1188.lv/katalogs/Restor%C4%81ni%2C%20%C4%93din%C4%81%C5%A1ana',
        'http://www.1188.lv/katalogs/Pic%C4%93rijas%2C%20%C4%93din%C4%81%C5%A1ana',
        'http://www.1188.lv/katalogs/%C4%92dienu%20pieg%C4%81de%2C%20%C4%93din%C4%81%C5%A1ana',
        'http://www.1188.lv/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3/%D0%9A%D0%B0%D1%84%D0%B5%2C%20%D0%BF%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%B5',
        'http://www.1188.lv/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3/%D0%A0%D0%B5%D1%81%D1%82%D0%BE%D1%80%D0%B0%D0%BD%2C%20%D0%BF%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%B5http://www.1188.lv/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3/%D0%A0%D0%B5%D1%81%D1%82%D0%BE%D1%80%D0%B0%D0%BD%2C%20%D0%BF%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%B5',
        'http://www.1188.lv/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3/%D0%9F%D0%B8%D1%86%D0%B5%D1%80%D0%B8%D1%8F%2C%20%D0%BF%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%B5',
        'http://www.1188.lv/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3/%D0%94%D0%BE%D1%81%D1%82%D0%B0%D0%B2%D0%BA%D0%B0%20%D0%B5%D0%B4%D1%8B%2C%20%D0%BF%D0%B8%D1%82%D0%B0%D0%BD%D0%B8%D0%B5'
    ]

    rules = (
        Rule(LinkExtractor(allow='.+1188\.lv\/catalog\/[\w\s%2C%20]*(\?page=\d+)*$', unique=True)),
        Rule(LinkExtractor(allow='.+1188\.lv\/katalogs\/[\w\s%2C%20]*(\?page=\d+)*$', unique=True)),
        Rule(
            LinkExtractor(allow='.+1188\.lv\/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3\/[\w\s%2C%20]*(\?page=\d+)*$',
                          unique=True)
        ),
        Rule(LinkExtractor(allow=[
            '.+1188\.lv\/catalog\/.+\/cafe-catering\/\d+$',
            '.+1188\.lv\/catalog\/.+\/restaurants-catering\/\d+$',
            '.+1188\.lv\/catalog\/.+\/pizzerias-catering\/\d+$',
            '.+1188\.lv\/catalog\/.+\/food-delivery-catering\/\d+$',
            '.+1188\.lv\/katalogs\/.+\/kafejnica-edinasana\/\d+$',
            '.+1188\.lv\/katalogs\/.+\/restorani-edinasana\/\d+$',
            '.+1188\.lv\/katalogs\/.+\/picerijas-edinasana\/\d+$',
            '.+1188\.lv\/katalogs\/.+\/dienu-piegade-edinasana\/\d+$',
            '.+1188\.lv\/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3\/.+\/afe-pitanie\/\d+$',
            '.+1188\.lv\/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3\/.+\/estoran-pitanie\/\d+$',
            '.+1188\.lv\/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3\/.+\/icerija-pitanie\/\d+$',
            '.+1188\.lv\/%D0%BA%D0%B0%D1%82%D0%B0%D0%BB%D0%BE%D0%B3\/.+\/ostavka-ed-pitanie\/\d+$',
        ], canonicalize=False, unique=True),
            callback='parse_company', follow=False
        ),
    )

    tr = {
        'ru': {
            u'описание': 'description',
            u'отрасль': 'branches',
            u'телефон': 'phone',
            u'е-почта': 'email',
            u'веб-страница': 'website',
            u'время работы': 'schedule',
            u'компания в соц. cети': 'socials',
            u'пн': 'monday',
            u'вт': 'tuesday',
            u'ср': 'wednesday',
            u'чт': 'thursday',
            u'пт': 'friday',
            u'сб': 'saturday',
            u'вс': 'sunday'
        },
        'lv': {
            u'apraksts': 'description',
            u'darbības nozares': 'branches',
            u'tālrunis': 'phone',
            u'e-pasts': 'email',
            u'mājas lapa': 'website',
            u'darba laiks': 'schedule',
            u'uzņēmums soc. tīklos': 'socials',
            u'p': 'monday',
            u'o': 'tuesday',
            u't': 'wednesday',
            u'c': 'thursday',
            u'p2': 'friday',
            u's': 'saturday',
            u'sv': 'sunday'
        },
        'en': {
            'description': 'description',
            'branches': 'branches',
            'phone': 'phone',
            'e-mail': 'email',
            'home page': 'website',
            'working hours': 'schedule',
            'company soc. profiles': 'socials',
            'mo': 'monday',
            'tu': 'tuesday',
            'we': 'wednesday',
            'th': 'thursday',
            'fr': 'friday',
            'sa': 'saturday',
            'su': 'sunday'
        }
    }

    def parse_company(self, response):
        def get_xpath_value(query, default=None):
            return utils.get_xpath_value(response, query, default)

        def get_css_value(query, default=None):
            return utils.get_css_value(response, query, default)

        def flatten(x):
            return list(itertools.chain.from_iterable(x))

        branch_id = int(response.url.split('/')[-1])
        lang = get_xpath_value('//html/@lang')
        description = get_css_value('p.description::text')
        short_description = get_xpath_value('//meta[contains(@name, "description")]/@content')

        headline = None
        name = ''.join(response.css('h1.title').xpath('//span[contains(@itemprop, "name")]/text()').extract()).strip()
        if name:
            m = re.search(r'^"(.*?)"(.*)$', name)
            if m:
                name = m.group(1).strip()
                headline = m.group(2).replace(',', ' ').strip()

        categories = response.css('#Info-Tab-Content > .row:last-child div.data strong::text').extract()
        categories = [c.lower().split(',') for c in categories]
        categories = flatten(categories)

        tags = response.css('li.keywords p.short > a::text').extract()
        tags += get_xpath_value('//meta[contains(@name, "keywords")]/@content', '').lower().split(' ')
        tags = set(filter(None, tags))

        address = get_css_value('div.address::text')

        contacts, schedule = self.extract_contacts_and_schedule(response, self.tr[lang])

        photos = ['http:' + p.extract() for p in response.css('.owl-carousel img::attr(src)')]
        logo = get_xpath_value('//img[contains(@itemprop, "logo")]/@src')
        if logo:
            logo = 'http:' + logo

        geometry = None
        latlng = response.css('script::text').re(r'"lat":"(.*)","long":"(.*)",')

        if latlng and len(latlng) > 1:
            try:
                geometry = {'point': {
                    'lat': float(latlng[0]),
                    'lng': float(latlng[1])
                }}
            except BaseException as e:
                self.log('%s %s' % (e, latlng), logging.WARN)

        return items.BranchItem(
            id=branch_id,
            name=utils.utf2str(name),
            headline=utils.utf2str(headline),
            lang=utils.utf2str(lang),
            url=utils.utf2str(response.url),
            categories=utils.utf2str(categories),
            address=utils.utf2str(address),
            full_description=utils.utf2str(description),
            short_description=utils.utf2str(short_description),
            contacts=utils.utf2str(contacts),
            schedule=utils.utf2str(schedule),
            logo=utils.utf2str(logo),
            photos=utils.utf2str(photos),
            geometry=geometry,
            tags=utils.utf2str(tags)
        )

    def extract_contacts_and_schedule(self, response, tr):
        contacts = list()
        schedule = dict()
        for li in response.css('#Info-Tab-Content > .row.first .items.left li'):
            for label in li.css('span.label::text'):
                label = label.extract().replace(':', '').strip().lower()
                label = tr.get(label, label)
                if label == 'phone':
                    for phone in li.css('.number::text'):
                        contacts.append({
                            'contact': label,
                            'value': phone.extract().strip()
                        })
                elif label == 'email':
                    for mail in li.css('.data > a::text'):
                        contacts.append({
                            'contact': label,
                            'value': mail.extract().strip()
                        })
                elif label == 'website':
                    for site in li.css('.data > a::text'):
                        contacts.append({
                            'contact': label,
                            'value': site.extract().strip()
                        })
                elif label == 'socials':
                    for sp in li.css('.data > a'):
                        contacts.append({
                            'contact': ''.join(sp.css('a::attr(class)').extract()).strip(),
                            'value': ''.join(sp.css('a::text').extract()).strip()
                        })
                elif label in ['description', 'branches']:
                    pass
                elif label == 'schedule':
                    for day in li.css('li.day'):
                        wd = ''.join(day.css('.Full::text').extract()).strip().lower()
                        if not wd:
                            continue
                        if wd in schedule:
                            wd += '2'
                        wd = tr[wd]
                        times = list(day.css('time::attr(datetime)'))
                        from_time = times[0].extract().strip()
                        to_time = times[1].extract().strip()
                        schedule[wd] = [{'from': str(from_time), 'to': str(to_time)}]
                else:
                    self.log('Unsupported contact %s' % label, logging.WARN)

        if not len(contacts):
            contacts = None
        if not len(schedule):
            schedule = None

        return contacts, schedule
