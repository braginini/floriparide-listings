package com.floriparide.listings.etl.parser.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class HttpConnector {

	public static String getPageAsString(String url, Map<String, String> formData) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);

		List<NameValuePair> nameValuePairs = new ArrayList<>(formData.size());

		for (Map.Entry<String, String> e : formData.entrySet()) {
			nameValuePairs.add(new BasicNameValuePair(e.getKey(), e.getValue()));
		}

		HttpContext localContext = new BasicHttpContext();

		CookieStore cookieStore = new BasicCookieStore();
		BasicClientCookie cookie = new BasicClientCookie("PHPSESSID", "bth1fleofu92sk6mkb0fa9oc12");
		cookieStore.addCookie(cookie);
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, Consts.UTF_8);
		post.setEntity(entity);

		CloseableHttpResponse response = httpClient.execute(post, localContext);
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			System.out.println("Response Status line :" + response.getStatusLine() + " " + url);
			return null;
		}

		try {
			HttpEntity resultEntity = response.getEntity();


			Document doc = Jsoup.parse(resultEntity.getContent(), null, url);

			return doc.toString();

		} finally {
			response.close();
			httpClient.close();
		}
	}

	public static Map<String, String> getBasicAbraselFormData() {
		Map<String, String> formData = new HashMap<>();
		formData.put("lista", "listGuiaBuscaFormEsquerda");
		formData.put("offsetlistGuiaBuscaFormEsquerda", "5");
		formData.put("s", "1");
		formData.put("classReference", "/view/GuiaBusca.php");
		formData.put("acao", "filtrar");
		formData.put("sest", "24");

		return formData;
	}
}
