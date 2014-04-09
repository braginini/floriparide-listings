package com.floriparide.listings.etl.parser;

import com.floriparide.listings.etl.parser.impl.ParseResultArchiveWorker;
import com.floriparide.listings.etl.parser.impl.foursquare.FoursquareClient;
import com.floriparide.listings.etl.parser.impl.foursquare.FoursquareParseManager;
import com.floriparide.listings.etl.parser.impl.foursquare.FoursquareVenueListWorker;
import com.floriparide.listings.etl.parser.impl.foursquare.FoursquareVenueProfileWorker;
import com.floriparide.listings.etl.parser.model.Worker;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public class FoursquareParserMain {

	public static void main(String[] args) throws IOException, InterruptedException {

		Worker archiveWorker = new ParseResultArchiveWorker(args[0]);
		FoursquareClient foursquareClient = FoursquareClient.getInstance(args[1], args[2], null);
		Worker profileWorker = new FoursquareVenueProfileWorker(foursquareClient, archiveWorker);
		Worker profileListWorker = new FoursquareVenueListWorker(foursquareClient, profileWorker);

		FoursquareParseManager threadManager = new FoursquareParseManager(profileListWorker, profileWorker, archiveWorker);
		threadManager.start();
	}
}
