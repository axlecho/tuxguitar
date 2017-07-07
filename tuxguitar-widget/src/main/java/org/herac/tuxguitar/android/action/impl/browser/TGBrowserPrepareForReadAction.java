package org.herac.tuxguitar.android.action.impl.browser;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.browser.model.TGBrowserSession;
import org.herac.tuxguitar.util.TGContext;

public class TGBrowserPrepareForReadAction extends TGActionBase{
	
	public static final String NAME = "action.browser.prepare-for-read";
	
	public static final String ATTRIBUTE_SESSION = TGBrowserSession.class.getName();
	
	public TGBrowserPrepareForReadAction(TGContext context) {
		super(context, NAME);
	}
	
	protected void processAction(final TGActionContext context) {
		TGBrowserSession tgBrowserSession = (TGBrowserSession) context.getAttribute(ATTRIBUTE_SESSION);
		tgBrowserSession.setSessionType(TGBrowserSession.READ_MODE);
	}
}
