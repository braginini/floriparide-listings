package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.fasterxml.jackson.databind.JsonNode;
import com.floriparide.listings.etl.parser.Abstract3TaskWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselProfileListParser;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileListWorker extends Abstract3TaskWorker<AbraselTask> {

	protected final static int shutDownTimeout = 100000; //ms

	public AbraselProfileListWorker(Worker abraselProfileWorker) {
		super(abraselProfileWorker);
	}

	@Override
	protected long getShutDownTimeout() {
		return shutDownTimeout;
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

						nextWorker.addTask(new Task<AbraselTask>() {
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

					totalDone.incrementAndGet();
					lastDoneTs.set(System.currentTimeMillis());

				} catch (Exception e) {
					log.error("Error while running list worker", e);
				}

			}
		});

	}
}
