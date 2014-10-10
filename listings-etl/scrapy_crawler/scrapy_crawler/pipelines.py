from scrapy.contrib.exporter import JsonLinesItemExporter
from scrapy.exceptions import DropItem


class JsonPaidItemPipeline(object):
    def __init__(self):
        self.files = {}
        self.items_seen = set()

    @classmethod
    def from_crawler(cls, crawler):
        pipeline = cls()
        return pipeline

    def open_spider(self, spider):
        file = open('/home/mike/parse_result/%s_paid_companies.json' % spider.name, 'w+b')
        self.files[spider] = file
        self.exporter = JsonLinesItemExporter(file, ensure_ascii=False)
        self.exporter.start_exporting()

    def close_spider(self, spider):
        self.exporter.finish_exporting()
        file = self.files.pop(spider)
        file.close()

    def process_item(self, item, spider):
        if item['name'] in self.items_seen:
            raise DropItem("Duplicate item found: %s" % item)
        else:
            self.exporter.export_item(item)
            self.items_seen.add(item['name'])
            return item
