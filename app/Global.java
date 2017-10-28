

import models.AreaInfo;
import play.*;
import play.Application;
import util.LocationLoader;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		super.onStart(app);
		if (!AreaInfo.hasArea()){
			LocationLoader loader = new LocationLoader();
			loader.loadJson();
		}
	}

	/*
	@Override

	public void onStart(Application app) {
		Logger.info("Application has started");
	}

	@Override
	public void onStop(Application app) {
		Logger.info("Application shutdown...");
		}
	*/
	    
}
