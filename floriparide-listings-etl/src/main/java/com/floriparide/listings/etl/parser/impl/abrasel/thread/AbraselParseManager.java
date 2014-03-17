package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.floriparide.listings.etl.AbstractParseManager;
import com.floriparide.listings.etl.parser.impl.ParseResultArchiveWorker;
import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Mikhail Bragin
 */
public class AbraselParseManager extends AbstractParseManager {

	String startUrl;

	String adminServiceUrl;

	public AbraselParseManager(Worker profileListWorker, Worker profileWorker, Worker archiveWorker,
	                           String startUrl, String adminServiceUrl) {
		super(profileListWorker, profileWorker, archiveWorker);
		this.adminServiceUrl = adminServiceUrl;
		this.startUrl = startUrl;
	}

	public void start() throws IOException, InterruptedException {
		super.start();

		for (int i = 0; i < getPageNumber(); i++)
			profileListWorker.addTask(getAbraselListTask(i));
	}

	private Task<AbraselTask> getAbraselListTask(final int page) {
		return new Task<AbraselTask>() {
			@Override
			public AbraselTask taskObject() {
				Map<String, String> formData = HttpConnector.getBasicAbraselFormData();
				formData.put("paginacao", String.valueOf(page));
				formData.put("lista", "listGuiaBuscaFormEsquerda");
				formData.put("offsetlistGuiaBuscaFormEsquerda", String.valueOf(5));
				formData.put("s", String.valueOf(1));
				formData.put("sest", String.valueOf(24));
				formData.put("classReference", "/view/GuiaBusca.php");
				formData.put("acao", "filtrar");

				return new AbraselTask(startUrl, formData);
			}
		};
	}

	private int getPageNumber() throws IOException {
		return 61;
	}

}
