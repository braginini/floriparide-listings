package com.floriparide.listings.etl.parser;

import com.floriparide.listings.etl.parser.model.Worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Mikhail Bragin
 */
public abstract class AbstractParseManager {

	ScheduledExecutorService shutdownPool;

	static int poolSize = 1;

	private static final Logger log = LoggerFactory.getLogger(AbstractParseManager.class);

	protected Worker archiveWorker;
	protected Worker profileWorker;
	protected Worker profileListWorker;

	public AbstractParseManager(Worker profileListWorker, Worker profileWorker, Worker archiveWorker) {
		this.archiveWorker = archiveWorker;
		this.profileWorker = profileWorker;
		this.profileListWorker = profileListWorker;

		this.shutdownPool = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "shutdown-worker");
			}
		});
	}

	public void start() throws Exception {

		//shutdown threads if no work
		shutdownPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

				log.info(profileListWorker.getClass().getSimpleName() + " submitted=" + profileListWorker.getSubmitted()
						+ " done=" + profileListWorker.getDone());
				log.info(profileWorker.getClass().getSimpleName() + " submitted=" + profileWorker.getSubmitted()
						+ " done=" + profileWorker.getDone());

				if (profileListWorker.shouldShutdown())
					profileListWorker.shutdown();

				if (profileWorker.shouldShutdown())
					profileWorker.shutdown();

				if (archiveWorker.shouldShutdown())
					archiveWorker.shutdown();

				if (profileListWorker.isStopped() && profileWorker.isStopped() && archiveWorker.isStopped()) {
					shutdownPool.shutdown();
					log.info("Stopped");
					for (Object o : ((Abstract3TaskWorker) profileWorker).getDubsSet()) {
						log.info(o.toString());
					}
					System.exit(1);
				}

			}
		}, 120, 120, TimeUnit.SECONDS);

	}
}
