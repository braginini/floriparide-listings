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
    Engine has a list of parsers and a pipeline (list of pipes).
    Each url (parse candidate) is testes by Parser.test(url) method and if success the page is downloaded and parsed
    Each parse result (Item) is processed in each pipe of a pipeline in order they were added (pipes).
    """

    def __init__(self, parsers=None, pipeline=None, parse_pool_size=1, pipeline_pool_size=1):
        self._parsers = parsers if parsers else []
        self._pipeline = pipeline if pipeline else []
        self._parse_pool = futures.ThreadPoolExecutor(max_workers=parse_pool_size)
        self._pipeline_pool = futures.ThreadPoolExecutor(max_workers=pipeline_pool_size)
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

    def pipeline(self, item):
        """
        goes through all defined pipes in a pipeline(_pipeline) in order pipes were added to engine and executes Item.pipe(item)
        :param item: Item the item to process in a pipeline
        :return:
        """
        def go_pipeline():
            if self._pipeline:
                for pipe in self._pipeline:
                    pipe.pipe(item)

        logging.info("Processing item %s in a pipeline" % str(item))
        self._pipeline_pool.submit(go_pipeline)

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
                self._futures[url] = self._parse_pool.submit(download_page, url, p)
                break

    def wait(self):
        """
        waits until all threads are complete
        """
        self._parse_pool.shutdown(True)
        self._pipeline_pool.shutdown(True)

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


class Pipe(object):
    """
    basic class for processing parsed result (Item object)
    classes that extend this class should override pipe() method
    Can be used for validating, saving results
    """

    def __init__(self):
        super(Pipe, self).__init__()

    def pipe(self, item):
        """

        :param item: Item to process in a pipe
        :return:
        """
        logging.info("Item %s in a pipe %s" % (self.__class__.__name__, str(item)))
        pass


class Item(object):
    """
    basic class for storing parse results.
    """

    def __init__(self):
        super(Item, self).__init__()

