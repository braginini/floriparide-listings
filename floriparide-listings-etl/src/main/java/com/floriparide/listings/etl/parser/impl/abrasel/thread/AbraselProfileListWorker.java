package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselProfileListParser;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileListWorker implements Worker<AbraselTask> {

	private static final Logger log = LoggerFactory.getLogger(AbraselProfileListWorker.class);

	ExecutorService executorService;

	AbraselProfileWorker abraselProfileWorker;

	final static int poolSize = 10;
	final static int shutDownTimeout = 10000; //ms

	boolean stopped = false;

	AtomicInteger totalDone = new AtomicInteger();
	AtomicLong lastDoneTs = new AtomicLong(System.currentTimeMillis());

	public AbraselProfileListWorker(AbraselProfileWorker abraselProfileWorker) {
		this.abraselProfileWorker = abraselProfileWorker;
		this.executorService = Executors.newFixedThreadPool(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "profile-list-worker");
			}
		});
	}

	@Override
	public void addTask(final Task<AbraselTask> task) {

		executorService.submit(new Runnable() {
			@Override
			public void run() {

				try {

					log.info("Started with task", task.taskObject());

					final AbraselTask abraselTask = task.taskObject();
					String html = HttpConnector.getPageAsString(abraselTask.getUrl(), abraselTask.getFormData());
					List<JsonNode> list = new AbraselProfileListParser().parse(html);

					for (JsonNode n : list) {
						final String id = n.get("estabId").textValue();

						abraselProfileWorker.addTask(new Task<AbraselTask>() {
							@Override
							public AbraselTask taskObject() {
								Map<String, String> formData = new HashMap<>();
								formData.put("id", String.valueOf(id));
								formData.put("classReference", "/view/DetalhamentoEstabelecimento.php");
								formData.put("modo", "editar");

								return new AbraselTask(abraselTask.getUrl(), formData);
							}
						});
					}

					totalDone.incrementAndGet();
					lastDoneTs.set(System.currentTimeMillis());

				} catch (Exception e) {
					log.error("Error while running list worker", e);
				}

			}
		});

	}

	@Override
	public void shutdown() {
		executorService.shutdown();

		try {
			while (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				log.info("Waiting for threads to finish");
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
