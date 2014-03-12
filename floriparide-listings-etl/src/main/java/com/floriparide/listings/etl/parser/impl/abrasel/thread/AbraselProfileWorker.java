package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.fasterxml.jackson.databind.JsonNode;
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

	AbraselParseResultArchiveWorker archiveWorker;

	final static int poolSize = 10;

	public AbraselProfileWorker(AbraselParseResultArchiveWorker archiveWorker) {
		this.executorService = Executors.newFixedThreadPool(poolSize);
		this.archiveWorker = archiveWorker;
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

					archiveWorker.addTask(new Task<JsonNode>() {
						@Override
						public JsonNode taskObject() {
							return node;
						}
					});

				} catch (IOException e) {
					log.error("Error while running profile worker", e);
				}

			}
		});


	}
}
