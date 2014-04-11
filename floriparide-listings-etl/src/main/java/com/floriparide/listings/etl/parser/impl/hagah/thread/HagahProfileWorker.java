package com.floriparide.listings.etl.parser.impl.hagah.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.Abstract3TaskWorker;
import com.floriparide.listings.etl.parser.impl.hagah.HagahProfileParser;
import com.floriparide.listings.etl.parser.model.ArchiveTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;
import com.floriparide.listings.model.RawData;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mikhail Bragin
 */
public class HagahProfileWorker extends Abstract3TaskWorker<String> {

	public static final String WORKER_NAME = "profile-worker";

	public HagahProfileWorker(Worker nextWorker) {
		super(nextWorker);
	}

	@Override
	protected long getShutDownTimeout() {
		return 100000;
	}

	@Override
	protected int getPoolSize() {
		return 30;
	}

	@Override
	protected String getWorkersName() {
		return WORKER_NAME;
	}

	@Override
	public void addTask(final Task<String> task) {
		if (set.contains(task.taskObject())) {
			log.warn("HagahProfileWorker Already did WTF??? " + task.taskObject());
			dubsSet.add(task.taskObject());
			return;
		} else {
			set.add(task.taskObject());
		}

		executorService.submit(new Runnable() {
			@Override
			public void run() {
				submitted.incrementAndGet();
				try {
					String url = task.taskObject();
					String resource = HttpConnector.getPageAsString(url, new HashMap<String, String>());
					if (resource != null) {
						HagahProfileParser parser = new HagahProfileParser();
						final JsonNode n = parser.parse(resource);
						if (n != null) {
							nextWorker.addTask(new Task() {
								@Override
								public Object taskObject() {
									return new ArchiveTask(RawData.Source.HAGAH, n);
								}
							});
						}
					} else {
						returnTask(task);
					}

					totalDone.incrementAndGet();
					lastDoneTs.set(System.currentTimeMillis());
					Thread.sleep(2000);

				} catch (IOException e) {
					log.error("Error while running profile worker " + task.taskObject(), e);
					submitted.decrementAndGet();
					returnTask(task);
				} catch (InterruptedException e) {
					log.error("Error while running profile worker " + task.taskObject(), e);
					submitted.decrementAndGet();
					returnTask(task);
				}
			}
		});
	}
}
