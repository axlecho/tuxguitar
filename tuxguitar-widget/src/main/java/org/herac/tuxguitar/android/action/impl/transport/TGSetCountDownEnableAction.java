package org.herac.tuxguitar.android.action.impl.transport;

import android.content.Context;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.persistence.TGPreferencesManager;
import org.herac.tuxguitar.util.TGContext;

public class TGSetCountDownEnableAction extends TGActionBase {
    public static final String NAME = "action.transport.countdown-enable";
    public static final String ATTRIBUTE_ENABLE = "countdown-enable";
    public TGSetCountDownEnableAction(TGContext context) {
        super(context, NAME);
    }

    @Override
    protected void processAction(TGActionContext context) {
        android.util.Log.d("axlecho","processAction  enable");
        boolean enable = context.getAttribute(ATTRIBUTE_ENABLE);
        TGPreferencesManager.getInstance(getContext()).setCountDownEnable(findContext(context), enable);
    }

    private Context findContext(TGActionContext context) {
        return context.getAttribute(TGActionBase.ATTRIBUTE_ACTIVITY);
    }
}
