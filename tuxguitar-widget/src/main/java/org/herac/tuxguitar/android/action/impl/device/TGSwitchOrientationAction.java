package org.herac.tuxguitar.android.action.impl.device;

import android.util.Log;

import org.herac.tuxguitar.android.device.ScreenManager;
import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.util.TGContext;


public class TGSwitchOrientationAction extends TGActionBase {
    public static final String NAME = "action.device.switchorientation";
    public static final String ATTRIBUTE_ACTIVITY = TGActivity.class.getName();

    public TGSwitchOrientationAction(TGContext context) {
        super(context, NAME);
    }

    @Override
    protected void processAction(TGActionContext context) {
        TGActivity tgActivity = (TGActivity) context.getAttribute(ATTRIBUTE_ACTIVITY);
        ScreenManager.getInstance(tgActivity.getApplicationContext()).switchOrientation(tgActivity);
    }
}
