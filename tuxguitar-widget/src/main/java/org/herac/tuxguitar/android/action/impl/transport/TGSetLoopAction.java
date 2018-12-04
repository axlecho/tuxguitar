package org.herac.tuxguitar.android.action.impl.transport;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.view.tablature.TGSongViewController;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.util.TGContext;

public class TGSetLoopAction extends TGActionBase {
    public static final String NAME = "action.transport.set-loop";

    public static final String ATTRIBUTE_X = "positionX";
    public static final String ATTRIBUTE_Y = "positionY";

    public TGSetLoopAction(TGContext context) {
        super(context, NAME);
    }

    protected void processAction(TGActionContext context){
        Float x = context.getAttribute(ATTRIBUTE_X);
        Float y = context.getAttribute(ATTRIBUTE_Y);

        if( x != null && y != null ) {
            TGMeasureImpl measure = getEditor().getAxisSelector().selectMeasure(x, y);
            TGSongViewController.getInstance(getContext()).getLoop().setLoop(measure);
        }
    }
}
