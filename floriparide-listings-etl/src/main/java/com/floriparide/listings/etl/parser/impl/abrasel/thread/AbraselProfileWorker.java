package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.floriparide.listings.etl.parser.impl.abrasel.AbraselProfileParser;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileWorker implements Worker<AbraselTask> {

	private static final Logger log = LoggerFactory.getLogger(AbraselProfileWorker.class);

	ExecutorService executorService;

	final static int poolSize = 10;

	public AbraselProfileWorker() {
		this.executorService = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void addTask(final Task<AbraselTask> task) {

		executorService.submit(new Runnable() {
			@Override
			public void run() {

				try {
					log.info("Started with task ", task.taskObject());
					String profile = HttpConnector.getPageAsString(task.taskObject().getUrl(), task.taskObject().getFormData());
					new AbraselProfileParser().parse(profile);

				} catch (IOException e) {
					log.error("Error while running profile worker", e);
				}

			}
		});


	}
}
