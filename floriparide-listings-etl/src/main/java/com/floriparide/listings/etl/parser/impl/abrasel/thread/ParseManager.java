package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.util.HttpConnector;

import java.io.IOException;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class ParseManager {

	String startUrl;

	AbraselProfileListWorker profileListWorker;

	public ParseManager(String startUrl, AbraselProfileListWorker profileListWorker) {
		this.startUrl = startUrl;
		this.profileListWorker = profileListWorker;
	}

	public void start() throws IOException {

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
