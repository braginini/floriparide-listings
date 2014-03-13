package com.floriparide.listings.etl.parser.model;

/**
 * @author mikhail.bragin
 * @since 8/10/13
 */
public interface Worker<T> {

	public void addTask(Task<T> task);

	public void shutdown();

	public boolean shouldShutdown();

	public boolean isStopped();
}
