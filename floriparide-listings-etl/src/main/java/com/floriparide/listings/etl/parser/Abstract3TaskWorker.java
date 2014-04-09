package com.floriparide.listings.etl.parser;

import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Basic class for profile list + profile + archive workers chain
 * @author Mikhail Bragin
 */
public abstract class Abstract3TaskWorker<T> implements Worker<T> {

	protected static final Logger log = LoggerFactory.getLogger(Abstract3TaskWorker.class);

	protected ExecutorService executorService;

	protected Worker nextWorker;

	protected final static int poolSize = 10;

	protected boolean stopped = false;

	protected AtomicInteger totalDone = new AtomicInteger();
	protected AtomicLong lastDoneTs = new AtomicLong(System.currentTimeMillis());

	public Abstract3TaskWorker(Worker nextWorker) {
		this.nextWorker = nextWorker;
		this.executorService = Executors.newFixedThreadPool(getPoolSize(), new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, getWorkersName());
			}
		});
	}

	@Override
	public boolean isStopped() {
		return stopped;
	}

	@Override
	public boolean shouldShutdown() {
		//should be shut down if last processed task was more than timeout ms ago
		if (System.currentTimeMillis() - lastDoneTs.get() > getShutDownTimeout())
			return true;

		return false;
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

	protected abstract long getShutDownTimeout();

	protected void returnTask(Task task) {
		log.warn("Resource was null, adding as a new task " + task.taskObject());
		addTask(task);
	}

	protected int getPoolSize() {
		return poolSize;
	}

	protected String getWorkersName() {
		return "worker";
	}
}
