package com.floriparide.listings.etl.parser.impl.foursquare;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floriparide.listings.etl.parser.impl.ParseResultArchiveWorker;
import com.floriparide.listings.etl.parser.model.ArchiveTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.model.RawData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.CompleteVenue;

/**
 * @author Mikhail Bragin
 */
public class FoursquareVenueProfileWorker implements Worker<CompactVenue> {

	private static final Logger log = LoggerFactory.getLogger(FoursquareVenueListWorker.class);

	FoursquareClient foursquareClient;

	ExecutorService executorService;

	Worker archiveWorker;

	ObjectMapper objectMapper;

	static final int poolSize = 10;

	public FoursquareVenueProfileWorker(FoursquareClient foursquareClient, Worker archiveWorker) {
		this.objectMapper = new ObjectMapper();
		this.foursquareClient = foursquareClient;
		this.archiveWorker = archiveWorker;
		this.executorService = Executors.newFixedThreadPool(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "4square-profile-worker");
			}
		});
	}

	@Override
	public void addTask(final Task<CompactVenue> task) {

		executorService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					Result<CompleteVenue> result = foursquareClient.getApi().venue(task.taskObject().getId());
					if (result.getMeta().getCode() == 200) {
						CompleteVenue venue = result.getResult();
						try {
							String json = objectMapper.writeValueAsString(venue);
							final JsonNode node = objectMapper.readValue(json, JsonNode.class);

							archiveWorker.addTask(new Task<ArchiveTask>() {
								@Override
								public ArchiveTask taskObject() {
									return new ArchiveTask(RawData.Source.FOURSQUARE, node);
								}
							});

						} catch (JsonProcessingException e) {
							log.error("Error while converting venue to JSON id=" + task.taskObject().getId(), e);
						} catch (IOException e) {
							log.error("Error while converting venue to JSON id=" + task.taskObject().getId(), e);
						}

					} else {
						log.error("Error while getting venue id=" + task.taskObject().getId() + " " +
								"code=" + result.getMeta().getCode() + " " +
								"type=" + result.getMeta().getErrorType() + " " +
								"detail=" + result.getMeta().getErrorDetail());
					}
				} catch (FoursquareApiException e) {
					log.error("Error while getting venue info venueId=" + task.taskObject().getId(), e);
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

	@Override
	public int getSubmitted() {
		return 0;
	}

	@Override
	public int getDone() {
		return 0;
	}
}
