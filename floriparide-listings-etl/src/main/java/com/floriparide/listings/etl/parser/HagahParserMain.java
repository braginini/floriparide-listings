package com.floriparide.listings.etl.parser;

import com.floriparide.listings.etl.parser.impl.ParseResultArchiveWorker;
import com.floriparide.listings.etl.parser.impl.hagah.thread.HagahParseManager;
import com.floriparide.listings.etl.parser.impl.hagah.thread.HagahProfileListWorker;
import com.floriparide.listings.etl.parser.impl.hagah.thread.HagahProfileWorker;

import java.io.IOException;
/**
 * @author Mikhail Bragin
 */
public class HagahParserMain {

	public static void main(String[] args) throws Exception {

		ParseResultArchiveWorker archiveWorker = new ParseResultArchiveWorker(args[1]);
		HagahProfileWorker profileWorker = new HagahProfileWorker(archiveWorker);
		HagahProfileListWorker profileListWorker = new HagahProfileListWorker(profileWorker);

		HagahParseManager parseManager = new HagahParseManager(profileListWorker, profileWorker, archiveWorker, args[0]);
		parseManager.start();

	}
}
