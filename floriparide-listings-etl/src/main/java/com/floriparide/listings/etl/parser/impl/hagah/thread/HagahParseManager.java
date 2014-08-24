package com.floriparide.listings.etl.parser.impl.hagah.thread;

import com.floriparide.listings.etl.parser.AbstractParseManager;
import com.floriparide.listings.etl.parser.impl.hagah.HagahCategoryParser;
import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileListPagesCountParser;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class HagahParseManager extends AbstractParseManager {

	String categoryPageUrl;

	public HagahParseManager(Worker profileListWorker, Worker profileWorker, Worker archiveWorker, String categoryPageUrl) {
		super(profileListWorker, profileWorker, archiveWorker);
		this.categoryPageUrl = categoryPageUrl;
	}

	public void start() throws Exception {
		super.start();

		String resource = null;
		while (resource == null) {
			resource = HttpConnector.getPageAsString(categoryPageUrl, new HashMap<String, String>());
		}

		//get all the categories urls and add up to PAGE_NUM pages
		//submit tasks to profile list parser
		HagahCategoryParser categoryParser = new HagahCategoryParser();
		List<String> categoryUrls = categoryParser.parse(resource);

		HagahProfileListPagesCountParser pagesCountParser = new HagahProfileListPagesCountParser();
		int total = 0;
		for (final String u : categoryUrls) {
			String categoryPage = null;
			while (categoryPage == null)
				categoryPage = HttpConnector.getPageAsString(u, new HashMap<String, String>());

			if (categoryPage != null) {
				int numPages = pagesCountParser.parse(categoryPage);
				for (int i = 1; i <= numPages; i++) {

					final int j = i;
					profileListWorker.addTask(new Task<String>() {
						@Override
						public String taskObject() {
							return u + ("&p=" + j);
						}
					});
					total++;
				}
			}

			Thread.sleep(500);
		}

		System.out.println("total="+ total);

	}
}
