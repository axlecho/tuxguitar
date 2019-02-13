package org.herac.tuxguitar.android.properties;

import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.properties.TGPropertiesManager;
import org.herac.tuxguitar.util.properties.TGPropertiesReader;
import org.herac.tuxguitar.util.properties.TGPropertiesWriter;

import android.app.Activity;

public class TGPropertiesAdapter {
	
	public static void initialize(TGContext context, Activity activity) {
		addFactory(context);
	}
	
	public static void addFactory(TGContext context) {
		TGPropertiesManager.getInstance(context).setPropertiesFactory(new TGPropertiesFactoryImpl());
	}
	
	public static void addReader(TGContext context, String resource, TGPropertiesReader reader) {
		TGPropertiesManager.getInstance(context).addPropertiesReader(resource, reader);
	}
	
	public static void addWriter(TGContext context, String resource, TGPropertiesWriter writer) {
		TGPropertiesManager.getInstance(context).addPropertiesWriter(resource, writer);
	}
}
