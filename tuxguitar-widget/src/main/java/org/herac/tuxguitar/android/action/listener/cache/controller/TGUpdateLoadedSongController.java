package org.herac.tuxguitar.android.action.listener.cache.controller;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.util.TGContext;

public class TGUpdateLoadedSongController extends TGUpdateItemsController {

	public TGUpdateLoadedSongController() {
		super();
	}

	@Override
	public void update(TGContext context, TGActionContext actionContext) {
		this.findUpdateBuffer(context).requestUpdateLoadedSong();
		
		super.update(context, actionContext);
	}
}
