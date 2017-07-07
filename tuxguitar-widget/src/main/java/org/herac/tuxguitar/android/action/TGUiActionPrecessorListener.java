package org.herac.tuxguitar.android.action;

import org.herac.tuxguitar.util.TGContext;

import java.util.Map;

public class TGUiActionPrecessorListener extends TGActionProcessorListener {
    public TGUiActionPrecessorListener(TGContext context, String actionName) {
        super(context, actionName);
    }

    public void processEvent(Object eventSource, Map<String, Object> attributes) {
        this.processOnCurrentThread(this.processEventAttributes(eventSource, attributes));
    }
}
