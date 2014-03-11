package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileWorker implements Worker<String> {

	private static final Logger log = LoggerFactory.getLogger(AbraselProfileWorker.class);

	ExecutorService executorService;

	final static int poolSize = 10;

	public AbraselProfileWorker() {
		this.executorService = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void addTask(final Task<String> task) {

		executorService.submit(new Runnable() {
			@Override
			public void run() {

				log.info("Started with task ", task.taskObject());

			}
		});


	}
}
