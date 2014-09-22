import logging
import re
from spider import Parser, Engine, Pipe, Item

__author__ = 'mikhail'

logging.basicConfig(format=u'[%(asctime)s] %(levelname)s %(filename)s:%(lineno)d# %(message)s',
                    level=logging.DEBUG)

re_digits = re.compile('\d+')


class TestPipe(Pipe):

    def pipe(self, item):
        logging.info('GOT U %s' % str(item))


class TestItem(Item):
    def __init__(self, name):
        self.name = name
        super().__init__()


class HagahCategoriesPageParser(Parser):
    """

    """

    def test(self, url):
        """
        overrides basic regexp behaviour by checking ending of the url
        :param url:
        :return:
        """
        return url.endswith('/guia/')

    def parse(self, soup, engine, url):
        links = soup.find_all('a')
        for link in links:
            logging.info("Found link %s" % str(link))
            if 'href' in link:
                engine.submit(link.get('href'))

        logging.info("Links count %d" % len(links))


class HagahCompanyListPageParser(Parser):
    """

    """

    def __init__(self):
        super().__init__(pattern_string='.+hagah\.com\.br\/\w+\/\w+\/guia\/\w+\?q=.+')

    def parse(self, soup, engine, url):
        links = soup.find_all('a')
        for link in links:
            logging.info("Found link %s" % str(link))
            if 'href' in link:
                engine.submit(link.get('href'))

        logging.info("Links count %d" % len(links))

        if '&p=' in url:
            return

        # get the number of results to build paging urls
        for span in soup.find_all('span', class_='spanResultado'):
            m = re_digits.match(span.text)
            if m:
                result_num = int(m.group())
                break

        if result_num:
            page_num = int(result_num / 20 + 1)
            for i in range(2, page_num + 1):
                new_url = url + '&p=%d' % i
                engine.submit(new_url)

if __name__ == "__main__":
    engine = Engine(
        parsers=[
            HagahCategoriesPageParser(),
            HagahCompanyListPageParser()
        ], pipeline=[
            TestPipe()
        ])
    engine.submit('http://www.hagah.com.br/sc/florianopolis/guia/acougues?q=;;c1400')
    engine.wait()