package com.floriparide.listings.model;

/**
 * Created by Mikhail Bragin
 */
public class Point {

	Double lat;
	Double lon;

	public Point(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
	}



	public Double getLat() {
		return lat;
	}

	public Double getLon() {
		return lon;
	}
}
