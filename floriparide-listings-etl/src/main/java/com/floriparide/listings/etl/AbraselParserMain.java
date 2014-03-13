package com.floriparide.listings.etl;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselParseResultArchiveWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselProfileListWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselProfileWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.ParseManager;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public class AbraselParserMain {

	public static void main(String[] args) throws IOException, InterruptedException {

		ParseManager threadManager = new ParseManager(args[0]);
		threadManager.start();
	}
}
