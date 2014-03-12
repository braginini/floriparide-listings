package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselProfileListParser;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileListWorker implements Worker<AbraselTask> {

	private static final Logger log = LoggerFactory.getLogger(AbraselProfileListWorker.class);

	ExecutorService executorService;

	AbraselProfileWorker abraselProfileWorker;

	final static int poolSize = 1;

	public AbraselProfileListWorker(AbraselProfileWorker abraselProfileWorker) {
		this.abraselProfileWorker = abraselProfileWorker;
		this.executorService = Executors.newFixedThreadPool(poolSize);
	}

	@Override
	public void addTask(final Task<AbraselTask> task) {

		executorService.submit(new Runnable() {
			@Override
			public void run() {

				try {

					log.info("Started with task", task.taskObject());

					final AbraselTask abraselTask = task.taskObject();
					String html = HttpConnector.getPageAsString(abraselTask.getUrl(), abraselTask.getFormData());
					List<JsonNode> list = new AbraselProfileListParser().parse(html);

					for (JsonNode n : list) {
						final String id = n.get("estabId").textValue();

						abraselProfileWorker.addTask(new Task<AbraselTask>() {
							@Override
							public AbraselTask taskObject() {
								Map<String, String> formData = new HashMap<>();
								formData.put("id", String.valueOf(id));
								formData.put("classReference", "/view/DetalhamentoEstabelecimento.php");
								formData.put("modo", "editar");

								return new AbraselTask(abraselTask.getUrl(), formData);
							}
						});
					}

				} catch (Exception e) {
					log.error("Error while running list worker", e);
				}

			}
		});

	}
}
