package org.herac.tuxguitar.android.action.impl.transport;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.transport.TGTransport;
import org.herac.tuxguitar.util.TGContext;

public class TGSetPlayRateAction extends TGActionBase {

    private static final String TAG = "TGSetPlayRateAction";
    public static final String NAME = "action.transport.setplayrate";
    public static final String ATTRIBUTE_PERCENT = "custompercent";

    public TGSetPlayRateAction(TGContext context) {
        super(context, NAME);
    }

    protected void processAction(TGActionContext context) {
        Integer percent = context.getAttribute(ATTRIBUTE_PERCENT);
        TGTransport.getInstance(getContext()).setPercent(percent);
        TGTransport.getInstance(getContext()).reset();
    }
}
