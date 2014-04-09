package com.floriparide.listings.etl.parser.impl.hagah.thread;

import com.floriparide.listings.etl.parser.AbstractParseManager;
import com.floriparide.listings.etl.parser.impl.hagah.HagahCategoryParser;
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

	public static final int PAGE_NUM = 150;

	public HagahParseManager(Worker profileListWorker, Worker profileWorker, Worker archiveWorker, String categoryPageUrl) {
		super(profileListWorker, profileWorker, archiveWorker);
		this.categoryPageUrl = categoryPageUrl;
	}

	public void start() throws IOException, InterruptedException {
		super.start();

		String resource = null;
		while (resource == null) {
			resource = HttpConnector.getPageAsString(categoryPageUrl, new HashMap<String, String>());
		}

		//get all the categories urls and add up to PAGE_NUM pages
		//submit tasks to profile list parser
		HagahCategoryParser categoryParser = new HagahCategoryParser();
		List<String> categoryUrls = categoryParser.parse(resource);

		for (final String u : categoryUrls) {
			for (int i = 1; i <= PAGE_NUM; i++) {
				final int j = i;
				profileListWorker.addTask(new Task<String>() {
					@Override
					public String taskObject() {
						return u + ("&p=" + j);
					}
				});
			}
		}

	}
}
