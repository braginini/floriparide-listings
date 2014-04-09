package com.floriparide.listings.etl.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.impl.ParseResultArchiveWorker;
import com.floriparide.listings.etl.parser.impl.hagah.HagahCategoryParser;
import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileListParser;
import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileParser;
import com.floriparide.listings.etl.parser.impl.hagah.thread.HagahParseManager;
import com.floriparide.listings.etl.parser.impl.hagah.thread.HagahProfileListWorker;
import com.floriparide.listings.etl.parser.impl.hagah.thread.HagahProfileWorker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class HagahParserMain {

	public static void main(String[] args) throws IOException, InterruptedException {

		//todo
		//1) run HagahCategoryParser
		//2) for each result url parse page by selecting element with class="navegacao"
		//   and selecting last element text of "aNormal imgPag" elements
		//3) construct urls for each page
		//4) send result to HagahProfileListParser

		ParseResultArchiveWorker archiveWorker = new ParseResultArchiveWorker(args[1]);
		HagahProfileWorker profileWorker = new HagahProfileWorker(archiveWorker);
		HagahProfileListWorker profileListWorker = new HagahProfileListWorker(profileWorker);

		HagahParseManager parseManager = new HagahParseManager(profileListWorker, profileWorker, archiveWorker, args[0]);
		parseManager.start();

	}
}
