package org.herac.tuxguitar.android.action.impl.gui;

import org.herac.tuxguitar.android.device.ScreenManager;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.util.TGContext;

public class TGFullScreenAction extends TGActionBase {
    public static final String NAME = "action.fullsceen.process";
    public static final String ATTRIBUTE_ACTIVITY = TGActivity.class.getName();

    public TGFullScreenAction(TGContext context) {
        super(context, NAME);
    }

    protected void processAction(final TGActionContext context) {
        TGActivity activity = context.getAttribute(ATTRIBUTE_ACTIVITY);
        ScreenManager.getInstance(activity.getApplicationContext()).swithFullScreen(activity);
    }
}
