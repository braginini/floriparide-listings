package com.floriparide.listings.etl.parser.impl.foursquare;

import com.floriparide.listings.etl.parser.AbstractParseManager;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;

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

    DecimalFormat df = new DecimalFormat("#.#");

    public FoursquareParseManager(Worker profileListWorker, Worker profileWorker, Worker archiveWorker) {
        super(profileListWorker, profileWorker, archiveWorker);
    }

    @Override
    public void start() throws IOException, InterruptedException {
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

    private List<Double[]> splitBounds(int level, double minLat, double minLng, double maxLat, double maxLng) {
        List<Double[]> result = new ArrayList<>();

        //if we reach a threshold (limit), we return current result
        if (level == levelThreshold) {
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
}
