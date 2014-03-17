package com.floriparide.listings.etl.parser.impl.foursquare;

import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;

/**
 * @author Mikhail Bragin
 */
public class FoursquareVenueListWorker implements Worker<Map<String, String>> {

	private static final Logger log = LoggerFactory.getLogger(FoursquareVenueListWorker.class);

	FoursquareClient foursquareClient;

	Worker profileWorker;

	ExecutorService executorService;

	static final int poolSize = 1;

	public FoursquareVenueListWorker(FoursquareClient foursquareClient, Worker profileWorker) {
		this.foursquareClient = foursquareClient;
		this.profileWorker = profileWorker;
		this.executorService = Executors.newFixedThreadPool(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "4square-list-worker");
			}
		});
	}

	@Override
	public void addTask(final Task<Map<String, String>> task) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {

				try {
					Result<VenuesSearchResult> result = foursquareClient.getApi().venuesSearch(task.taskObject());
					if (result.getMeta().getCode() == 200) {
						for (final CompactVenue r : result.getResult().getVenues()) {
							profileWorker.addTask(new Task<CompactVenue>() {
								@Override
								public CompactVenue taskObject() {
									return r;
								}
							});
						}
					} else {
						log.error("Error while getting venue list code=" + result.getMeta().getCode() +
								" type=" + result.getMeta().getErrorType() + " " +
								"detail=" + result.getMeta().getErrorDetail());
					}

				} catch (FoursquareApiException e) {
					log.error("Error while getting venues list", e);
				}
			}
		});
	}

	@Override
	public void shutdown() {

	}

	@Override
	public boolean shouldShutdown() {
		return false;
	}

	@Override
	public boolean isStopped() {
		return false;
	}
}
