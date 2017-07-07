package org.herac.tuxguitar.android.action.impl.browser;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.action.TGActionException;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.browser.TGBrowserManager;
import org.herac.tuxguitar.android.browser.model.TGBrowserException;
import org.herac.tuxguitar.util.TGContext;

public class TGBrowserCloseSessionAction extends TGActionBase{
	
	public static final String NAME = "action.browser.close-session";
	
	public TGBrowserCloseSessionAction(TGContext context) {
		super(context, NAME);
	}
	
	protected void processAction(final TGActionContext context) {
		try {
			TGBrowserManager tgBrowserManager = TGBrowserManager.getInstance(getContext());
			tgBrowserManager.closeSession();
		} catch (TGBrowserException e) {
			throw new TGActionException(e);
		}
	}
}
