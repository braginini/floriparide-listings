package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
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
public class ParseManager {

	String startUrl;

	AbraselProfileListWorker profileListWorker;
	AbraselProfileWorker profileWorker;
	AbraselParseResultArchiveWorker archiveWorker;

	ScheduledExecutorService shutdownPool;

	static int poolSize = 1;

	private static final Logger log = LoggerFactory.getLogger(ParseManager.class);

	public ParseManager(String startUrl) {
		this.startUrl = startUrl;
		this.archiveWorker = new AbraselParseResultArchiveWorker();
		this.profileWorker = new AbraselProfileWorker(archiveWorker);
		this.profileListWorker = new AbraselProfileListWorker(profileWorker);

		this.shutdownPool = new ScheduledThreadPoolExecutor(poolSize, new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "shutdown-worker");
			}
		});
	}

	public void start() throws IOException, InterruptedException {

		for (int i = 0; i < getPageNumber(); i++)
			profileListWorker.addTask(getAbraselListTask(i));

		//shutdown threads if no work
		shutdownPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

				if (profileListWorker.shouldShutdown())
					profileListWorker.shutdown();

				if (profileWorker.shouldShutdown())
					profileWorker.shutdown();

				if (archiveWorker.shouldShutdown())
					archiveWorker.shutdown();

				if (profileListWorker.isStopped() && profileWorker.isStopped() && archiveWorker.isStopped()) {
					shutdownPool.shutdown();
					System.exit(1);
				}

			}
		}, 20, 20, TimeUnit.SECONDS);

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
