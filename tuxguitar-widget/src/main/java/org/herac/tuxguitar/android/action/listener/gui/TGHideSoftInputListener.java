package org.herac.tuxguitar.android.action.listener.gui;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.event.TGEvent;
import org.herac.tuxguitar.event.TGEventListener;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class TGHideSoftInputListener implements TGEventListener {
	
	public static final String ATTRIBUTE_BY_PASS = (TGHideSoftInputListener.class.getName() + "-byPass");
	public static final String ATTRIBUTE_DONE = (TGHideSoftInputListener.class.getName() + "-done");
	
	private TGActivity activity;
	
	public TGHideSoftInputListener(TGActivity activity){
		this.activity = activity;
	}
	
	public void hideSoftInputFromWindow() {
		View view = this.activity.getCurrentFocus();
		if( view != null ) {  
			InputMethodManager imm = (InputMethodManager) this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
	public void checkForHideSoftInputFromWindow(TGEvent event) {
		TGActionContext actionContext = this.getActionContext(event);
		if(!Boolean.TRUE.equals(actionContext.getAttribute(ATTRIBUTE_DONE))) {
			hideSoftInputFromWindow();
			actionContext.setAttribute(ATTRIBUTE_DONE, Boolean.TRUE);
		}
	}
	
	public TGActionContext getActionContext(TGEvent event) {
		return event.getAttribute(TGEvent.ATTRIBUTE_SOURCE_CONTEXT);
	}
	
	public boolean isByPassInContext(TGActionContext context) {
		return Boolean.TRUE.equals(context.getAttribute(ATTRIBUTE_BY_PASS));
	}
	
	public boolean isByPassInContext(TGEvent event) {
		return this.isByPassInContext(this.getActionContext(event));
	}
	
	public void processEvent(TGEvent event) {
		if(!this.isByPassInContext(event) ) {
			this.checkForHideSoftInputFromWindow(event);
		}
	}
}
