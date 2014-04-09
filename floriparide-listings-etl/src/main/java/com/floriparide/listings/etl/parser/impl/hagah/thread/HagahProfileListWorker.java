package com.floriparide.listings.etl.parser.impl.hagah.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.Abstract3TaskWorker;
import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileListParser;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class HagahProfileListWorker extends Abstract3TaskWorker<String> {

	public HagahProfileListWorker(Worker nextWorker) {
		super(nextWorker);
	}

	@Override
	protected long getShutDownTimeout() {
		return 100000;
	}

	@Override
	protected int getPoolSize() {
		return 10;
	}

	@Override
	public void addTask(final Task<String> task) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					final String url = task.taskObject();
					String resource = HttpConnector.getPageAsString(url, new HashMap<String, String>());
					if (resource != null) {
						HagahProfileListParser parser = new HagahProfileListParser();
						List<String> profileUrls = parser.parse(resource);

						for (final String u : profileUrls) {
							nextWorker.addTask(new Task() {
								@Override
								public Object taskObject() {
									return u;
								}
							});
						}
					} else {
						returnTask(task);
					}
				} catch (IOException e) {
					log.error("Error while running list worker", e);
				}
			}
		});
	}
}
