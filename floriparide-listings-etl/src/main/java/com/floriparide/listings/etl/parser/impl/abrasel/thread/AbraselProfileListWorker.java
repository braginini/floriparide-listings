package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mikhail Bragin
 */
public class AbraselProfileListWorker implements Worker<AbraselTask> {

	private static final Logger log = LoggerFactory.getLogger(AbraselProfileListWorker.class);

	ExecutorService executorService;

	AbraselProfileWorker abraselProfileWorker;


	final static int poolSize = 10;

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

					AbraselTask abraselTask = task.taskObject();
					String html = HttpConnector.getPageAsString(abraselTask.getUrl(), abraselTask.getFormData());

					Document doc = Jsoup.parse(html);
					Element resultEl = doc.getElementById("listagemlistGuiaBuscaFormEsquerda");

					if (resultEl != null) {
						Elements listItemEls = resultEl.getElementsByClass("lista-estab-item");

						if (listItemEls != null) {

							for (Element itemEl : listItemEls) {

								Elements hrefEls = itemEl.getElementsByClass("estab");
								if (hrefEls != null && !hrefEls.isEmpty()) {

									Element hrefEl = hrefEls.get(0);
									final String url = hrefEl.attr("href");

									if (url != null) {
										abraselProfileWorker.addTask(new Task<String>() {
											@Override
											public String taskObject() {
												return url;
											}
										});
									}

								}
							}
						}

					} else {
						log.info("No result list on page", abraselTask.toString());
					}


				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});

	}
}
