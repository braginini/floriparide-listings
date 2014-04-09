package com.floriparide.listings.etl.parser;
import com.floriparide.listings.etl.parser.impl.ParseResultArchiveWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselParseManager;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselProfileListWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselProfileWorker;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public class AbraselParserMain {

	public static void main(String[] args) throws IOException, InterruptedException {

		ParseResultArchiveWorker archiveWorker = new ParseResultArchiveWorker(args[1]);
		AbraselProfileWorker profileWorker = new AbraselProfileWorker(archiveWorker);
		AbraselProfileListWorker profileListWorker = new AbraselProfileListWorker(profileWorker);

		AbraselParseManager threadManager = new AbraselParseManager(profileListWorker, profileWorker, archiveWorker, args[0], args[1]);
		threadManager.start();
		//new AbraselWorkingHoursParser().parse("Segunda à Sexta, de 12:00 ate 15:00. Segunda à Quinta, de 18:00 ate 23:00. Sabado, de 12:00 ate 01:00. Domingo, de 12:00 ate 23:00. Sexta, de 18:00 ate 01:00.");
	}
}
