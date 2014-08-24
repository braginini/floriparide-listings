package com.floriparide.listings.etl.parser.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.floriparide.listings.etl.parser.model.ArchiveTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
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
public class ParseResultArchiveWorker implements Worker<ArchiveTask> {

	private static final Logger log = LoggerFactory.getLogger(ParseResultArchiveWorker.class);

	ScheduledExecutorService executorService;

	final static int poolSize = 1;
	final static int period = 10;
	AtomicInteger totalDone = new AtomicInteger();
	AtomicInteger noWork = new AtomicInteger();
	JsonNodeFactory factory;
	String adminServiceUrl;

	boolean stopped = false;

	BlockingQueue<ArchiveTask> batch;

	public ParseResultArchiveWorker(String adminServiceUrl) {
		this.adminServiceUrl = adminServiceUrl;
		this.executorService = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "archive-worker");
			}
		});

		this.batch = new LinkedBlockingQueue<>();
		this.factory = JsonNodeFactory.instance;
		start();
	}

	@Override
	public void addTask(Task<ArchiveTask> task) {
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

					List<ArchiveTask> currentBatch = new ArrayList<>(batch.size());
					batch.drainTo(currentBatch);

					ArrayNode arrayNode = factory.arrayNode();

					for (ArchiveTask n : currentBatch) {
						ObjectNode objectNode = factory.objectNode();
						objectNode.put("data", n.getNode().toString());
						objectNode.put("source", n.getSource().getSource());
						arrayNode.add(objectNode);
					}

					ObjectNode entitiesNode = factory.objectNode();
					entitiesNode.put("entities", arrayNode);

					CloseableHttpClient httpClient = HttpClients.createDefault();
					HttpPost post = new HttpPost(adminServiceUrl);
					//post.addHeader("Content-Type", "application/json");
					HttpResponse response;
					StringEntity requestEntity;
					try {
						requestEntity = new StringEntity(entitiesNode.toString(), Charset.forName("UTF-8"));
						requestEntity.setContentEncoding("UTF-8");
						requestEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
						post.setEntity(requestEntity);

						response = httpClient.execute(post);
					} catch (UnsupportedEncodingException e) {
						log.error("Error while creating request entity", e);
					} catch (ClientProtocolException e) {
						log.error("Error while sending request", e);
					} catch (IOException e) {
						log.error("Error while sending request", e);
					}


					totalDone.addAndGet(currentBatch.size());
				}
			}, 0, period, TimeUnit.SECONDS);
		}
	}

	public boolean shouldShutdown() {
		//checks if the worker has no work for last 2 minutes
		/*if (noWork.get() * period >= 2 * 60)
			return true;*/

		return false;
	}

	@Override
	public boolean isStopped() {
		return stopped;
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
