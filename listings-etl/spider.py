from concurrent import futures
import logging
import re
from threading import Lock
from time import sleep

import requests
from bs4 import BeautifulSoup


__author__ = 'mikhail'


class Engine(object):
    """
    core spider stuff
    """

    def __init__(self, parsers=None, thread_pool_size=1):
        self._parsers = parsers if parsers else []
        self._thread_pool = futures.ThreadPoolExecutor(max_workers=thread_pool_size)
        #key - url, value - result future
        self._futures = {}
        self._lock = Lock()
        super(Engine, self).__init__()

    def add_parser(self, parser):
        """
        registers parser instance at engine
        :param parser:
        :return:
        """
        self._parsers.append(parser)

    def submit(self, url):
        """
        goes through all defined parsers(in _parsers) and submits URL for execution by first matching parser
        :param url: the  url to submit
        :return:
        """
        if url in self._futures:
            return

        def download_page(url, parser):
            """
            downloads page under given url and sends result HTML to parser
            makes 5 attempts to download page
            :param url: str the  url to download
            :param parser Parser the instance of a parser
            :return:
            """

            with self._lock:

                for i in range(5):
                    try:
                        logging.info("Starting download of url %s attempt %d" % (url, i))
                        html = requests.get(url).text
                        logging.info("Successfully downloaded page for url %s " % url)
                    except requests.exceptions.HTTPError as e:
                        logging.error("Error fetching url %s" % url, e)
                        sleep(5)
                        continue
                    if not html:
                        logging.error("Strange response %s" % url)
                        return

                    soup = BeautifulSoup(html)
                    parser.parse(soup, self, url)
                    break

        for p in self._parsers:
            if p.test(url):
                logging.info("Found proper parser %s and submitting url %s for processing" % (p.name, url))
                self._futures[url] = self._thread_pool.submit(download_page, url, p)
                break

    def wait(self):
        """
        waits until all threads are complete
        """
        self._thread_pool.shutdown(True)

        logging.info("Processed %d pages" % len(self._futures))


class Parser(object):
    """
    base class for all parsers
    """

    def __init__(self, name=None, pattern_string=None):
        self._name = name if name else self.__class__.__name__
        if pattern_string:
            self._pattern = re.compile(pattern_string)
        super(Parser, self).__init__()

    @property
    def name(self):
        return self._name

    def test(self, url):
        """
        tests url
        :param url: str
        :return: bool true if url matched
        """
        return self._pattern.match(url)

    def parse(self, soup, engine, url):
        """
        :param soup: BeautifulSoup
        :param engine: Engine
        :return:
        """
        pass

