package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselProfileParser;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileWorker implements Worker<AbraselTask> {

	private static final Logger log = LoggerFactory.getLogger(AbraselProfileWorker.class);

	ExecutorService executorService;

	AbraselParseResultArchiveWorker archiveWorker;
	AtomicLong lastDoneTs = new AtomicLong(System.currentTimeMillis());
	AtomicLong totalDone = new AtomicLong();

	boolean stopped = false;

	final static int shutDownTimeout = 100000; //ms

	final static int poolSize = 50;

	public AbraselProfileWorker(AbraselParseResultArchiveWorker archiveWorker) {
		this.executorService = Executors.newFixedThreadPool(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "profile-worker");
			}
		});

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

					totalDone.incrementAndGet();
					lastDoneTs.set(System.currentTimeMillis());

				} catch (IOException e) {
					log.error("Error while running profile worker", e);
				}

			}
		});


	}

	@Override
	public void shutdown() {
		executorService.shutdown();

		try {
			while (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				log.info("Awaiting for threads to complete");
			}
		} catch (InterruptedException e) {
			log.error("Error while shutting down", e);
		}

		stopped = true;
		log.info("All threads have stopped, totalDone=" + totalDone.get());
	}

	@Override
	public boolean shouldShutdown() {

		//should be shut down if last processed task was more than timeout ms ago
		if (System.currentTimeMillis() - lastDoneTs.get() > shutDownTimeout)
			return true;

		return false;
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}
}
