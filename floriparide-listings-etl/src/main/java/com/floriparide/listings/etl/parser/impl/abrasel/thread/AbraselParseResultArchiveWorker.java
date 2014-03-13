package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mikhail Bragin
 */
public class AbraselParseResultArchiveWorker implements Worker<JsonNode> {

	private static final Logger log = LoggerFactory.getLogger(AbraselParseResultArchiveWorker.class);

	ScheduledExecutorService executorService;

	final static int poolSize = 1;
	final static int period = 20;
	AtomicInteger totalDone = new AtomicInteger();
	AtomicInteger noWork = new AtomicInteger();

	boolean stopped = false;

	BlockingQueue<JsonNode> batch;

	public AbraselParseResultArchiveWorker() {
		this.executorService = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "archive-worker");
			}
		});

		this.batch = new LinkedBlockingQueue<>();
		start();
	}

	@Override
	public void addTask(Task<JsonNode> task) {
		try {
			batch.put(task.taskObject());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	private void start() {
		for (int i = 0; i < poolSize; i++) {
			executorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {

					if (batch.isEmpty()) {
						noWork.incrementAndGet();
						return;
					}

					noWork.set(0);

					List<JsonNode> currentBatch = new ArrayList<>(batch.size());
					batch.drainTo(currentBatch);

					totalDone.addAndGet(currentBatch.size());
					System.out.println(currentBatch.size());
					System.out.println(totalDone.get());
				}
			}, 0, period, TimeUnit.SECONDS);
		}
	}

	public boolean shouldShutdown() {
		//checks if the worker has no work for last 5 minutes
		if (noWork.get() * period >= 10)
			return true;

		return false;
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}
}
