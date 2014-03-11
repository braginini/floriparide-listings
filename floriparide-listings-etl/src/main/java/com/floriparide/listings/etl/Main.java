package com.floriparide.listings.etl;

import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselProfileListWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.AbraselProfileWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.thread.ParseManager;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public class Main {

	public static void main(String[] args) throws IOException {
		AbraselProfileWorker profileWorker = new AbraselProfileWorker();
		AbraselProfileListWorker listWorker = new AbraselProfileListWorker(profileWorker);
		ParseManager threadManager = new ParseManager(args[0], listWorker);
		threadManager.start();
	}
}
