package com.floriparide.listings.etl.parser.impl.foursquare;

import com.floriparide.listings.etl.parser.AbstractParseManager;
import com.floriparide.listings.etl.parser.model.Task;
import com.floriparide.listings.etl.parser.model.Worker;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author Mikhail Bragin
 */
public class FoursquareParseManager extends AbstractParseManager {

	public FoursquareParseManager(Worker profileListWorker, Worker profileWorker, Worker archiveWorker) {
		super(profileListWorker, profileWorker, archiveWorker);
	}

	@Override
	public void start() throws IOException, InterruptedException {
		super.start();

		profileListWorker.addTask(new Task() {
			@Override
			public Object taskObject() {
				HashMap<String, String> params = new HashMap<>();
				params.put("near", "Florianópolis");
				return params;
			}
		});
	}
}
