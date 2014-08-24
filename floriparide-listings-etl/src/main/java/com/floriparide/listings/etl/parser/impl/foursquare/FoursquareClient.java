package com.floriparide.listings.etl.parser.impl.foursquare;

import fi.foyt.foursquare.api.FoursquareApi;

/**
 * @author Mikhail Bragin
 */
public class FoursquareClient {

	FoursquareApi api;

	static FoursquareClient instance;

	protected FoursquareClient(String clientId, String clientSecret, String callbackUrl) {
		this.api = new FoursquareApi(clientId, clientSecret, callbackUrl);
        this.api.setVersion("20140823");
	}

	public static FoursquareClient getInstance(String clientId, String clientSecret, String callbackUrl) {
		if (instance == null)
			instance = new FoursquareClient(clientId, clientSecret, callbackUrl);

		return instance;
	}

	public FoursquareApi getApi() {
		return api;
	}

	public void setApi(FoursquareApi api) {
		this.api = api;
	}
}
