import logging
from spider import Parser, Engine

__author__ = 'mikhail'

logging.basicConfig(format=u'[%(asctime)s] %(levelname)s %(filename)s:%(lineno)d# %(message)s',
                    level=logging.DEBUG)


class HagahCategoriesPageParser(Parser):
    """

    """

    def __init__(self,):
        super().__init__('HagahCategoriesPageParser')

    def test(self, url):
        """
        overrides basic regexp behaviour by checking ending of the url
        :param url:
        :return:
        """
        return url.endswith('/guia/')

    def parse(self, soup, engine):
        links = soup.find_all('a')
        for link in links:
            logging.info("Found link %s" % str(link))
            if 'href' in link:
                engine.submit(link.get('href'))

        logging.info("Links count %d" % len(links))

if __name__ == "__main__":
    parser = HagahCategoriesPageParser()
    engine = Engine([parser])
    engine.submit('http://www.hagah.com.br/sc/florianopolis/guia/')
    engine.wait()


