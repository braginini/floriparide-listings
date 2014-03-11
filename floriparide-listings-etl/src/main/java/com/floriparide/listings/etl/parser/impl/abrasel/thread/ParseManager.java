package com.floriparide.listings.etl.parser.impl.abrasel.thread;

import com.floriparide.listings.etl.parser.impl.abrasel.AbraselTask;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.util.HttpConnector;
import com.sun.org.apache.regexp.internal.recompile;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
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
				return new AbraselTask(startUrl, formData);
			}
		};
	}

	private int getPageNumber() throws IOException {

		Map<String, String> formData = new HashMap<>();

		String html = HttpConnector.getPageAsString(startUrl, formData);

		Document doc = Jsoup.parse(html);

		Elements pageEls = doc.getElementsByClass("totalPaginas destaqueCorLista");
		Element el = pageEls.get(0);

		return Integer.valueOf(el.text());
	}

}
