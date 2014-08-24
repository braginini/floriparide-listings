package com.floriparide.listings.etl.parser.impl.foursquare;

import com.floriparide.listings.etl.parser.AbstractParseManager;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;
import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class FoursquareParseManager extends AbstractParseManager {

    int levelThreshold = 5;

    double diagonalThreshold = 3.0; //km

    DecimalFormat df = new DecimalFormat("#.#");

    public FoursquareParseManager(Worker profileListWorker, Worker profileWorker, Worker archiveWorker) {
        super(profileListWorker, profileWorker, archiveWorker);
    }

    @Override
    public void start() throws Exception {
        super.start();
        List<Double[]> result = splitBounds(0, -27.8, -48.7, -27.2, -48.3);
        for (final Double[] d : result) {

            profileListWorker.addTask(new Task() {
                @Override
                public Object taskObject() {
                    String sw = df.format(d[0]) + "," + df.format(d[1]);
                    String ne = df.format(d[2]) + "," + df.format(d[3]);
                    HashMap<String, String> params = new HashMap<>();
                    params.put("sw", sw);//lat,lng
                    params.put("ne", ne);
                    params.put("intent", "browse");
                    params.put("limit", "200");
                    return params;
                }
            });

        }
    }

    private List<Double[]> splitBounds(int level, double minLat, double minLng, double maxLat, double maxLng) throws FactoryException, TransformException {
        List<Double[]> result = new ArrayList<>();
        //if we reach a threshold (limit), we return current result
        double diagonal = distance(minLat, minLng, maxLat, maxLng, 'K');
        if (diagonal <= diagonalThreshold) {
            result.add(new Double[]{minLat, minLng, maxLat, maxLng});
            return result;
        }

        // calculate the new latitude and longitude
        double newLat = (minLat + maxLat) / 2;
        double newLng = (minLng + maxLng) / 2;

        level++;
        result.addAll(splitBounds(level, newLat, minLng, maxLat, newLng));

        result.addAll(splitBounds(level, newLat, newLng, maxLat, maxLng));

        result.addAll(splitBounds(level, minLat, minLng, newLat, newLng));

        result.addAll(splitBounds(level, minLat, newLng, newLat, maxLng));

        return result;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
