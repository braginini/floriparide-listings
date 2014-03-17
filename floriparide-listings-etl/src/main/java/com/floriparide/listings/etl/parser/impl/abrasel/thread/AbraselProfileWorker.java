package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.Abstract3TaskWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselProfileParser;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileWorker extends Abstract3TaskWorker<AbraselTask> {

	protected final static int shutDownTimeout = 100000; //ms

	public AbraselProfileWorker(Worker archiveWorker) {
		super(archiveWorker);
	}

	@Override
	protected long getShutDownTimeout() {
		return shutDownTimeout;
	}

	@Override
	public void addTask(final Task<AbraselTask> task) {

		executorService.submit(new Runnable() {
			@Override
			public void run() {

				try {
					log.info("Started with task ", task.taskObject());
					String profile = HttpConnector.getPageAsString(task.taskObject().getUrl(), task.taskObject().getFormData());
					final JsonNode node = new AbraselProfileParser().parse(profile);

					nextWorker.addTask(new Task<JsonNode>() {
						@Override
						public JsonNode taskObject() {
							return node;
						}
					});

					totalDone.incrementAndGet();
					lastDoneTs.set(System.currentTimeMillis());

				} catch (IOException e) {
					log.error("Error while running profile worker", e);
				}

			}
		});
	}
}
