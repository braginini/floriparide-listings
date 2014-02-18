package com.floriparide.listings.model;

/**
 * Created by Mikhail Bragin
 */
public class Point {

	double lat;
	double lon;

	public Point(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
}
