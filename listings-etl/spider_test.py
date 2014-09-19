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
        for link in soup.find_all('a'):
            if 'href' in link:
                engine.submit(link.get('href'))

if __name__ == "__main__":
    parser = HagahCategoriesPageParser()
    engine = Engine([parser])
    engine.submit('http://www.hagah.com.br/sc/florianopolis/guia/')


