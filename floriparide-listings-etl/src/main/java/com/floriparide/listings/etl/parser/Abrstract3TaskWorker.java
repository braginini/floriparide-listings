package com.floriparide.listings.etl.parser;

import com.floriparide.listings.etl.parser.model.Worker;

/**
 * Basic class for profile list + profile + archive workers chain
 * @author Mikhail Bragin
 */
public abstract class Abrstract3TaskWorker<T> implements Worker<T> {

	@Override
	public boolean isStopped() {
		return false;
	}

	@Override
	public boolean shouldShutdown() {
		return false;
	}

	@Override
	public void shutdown() {

	}
}
