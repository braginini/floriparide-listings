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
import java.util.concurrent.TimeUnit;

/**
 * @author Mikhail Bragin
 */
public class AbraselParseResultArchiveWorker implements Worker<JsonNode> {

	private static final Logger log = LoggerFactory.getLogger(AbraselParseResultArchiveWorker.class);

	ScheduledExecutorService executorService;

	final static int poolSize = 1;
	final static int period = 20;

	BlockingQueue<JsonNode> batch;

	public AbraselParseResultArchiveWorker() {
		this.executorService = new ScheduledThreadPoolExecutor(poolSize);
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

	private void start() {
		for (int i = 0; i < poolSize; i++) {
			executorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (batch.isEmpty())
						return;

					List<JsonNode> currentBatch = new ArrayList<>(batch.size());
					batch.drainTo(currentBatch);

					System.out.println(currentBatch.size());
				}
			}, 0, period, TimeUnit.SECONDS);
		}
	}
}
