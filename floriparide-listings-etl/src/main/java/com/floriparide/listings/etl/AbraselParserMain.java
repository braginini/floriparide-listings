package com.floriparide.listings.etl;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselWorkingHoursParser;
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

		/*ParseManager threadManager = new ParseManager(args[0], args[1]);
		threadManager.start();*/
		new AbraselWorkingHoursParser().parse("Segunda à Sexta, de 12:00 ate 15:00. Segunda à Quinta, de 18:00 ate 23:00. Sabado, de 12:00 ate 01:00. Domingo, de 12:00 ate 23:00. Sexta, de 18:00 ate 01:00.");
	}
}
