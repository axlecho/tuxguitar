package org.herac.tuxguitar.android.action.impl.intent;

import android.content.Intent;
import android.util.Log;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.util.TGContext;

public class TGProcessTitleAction extends TGActionBase {
    public static final String NAME = "action.title.process";
    public static final String ATTRIBUTE_ACTIVITY = TGActivity.class.getName();

    public TGProcessTitleAction(TGContext context) {
        super(context, NAME);
    }

    protected void processAction(final TGActionContext context) {
        TGActivity activity = context.getAttribute(ATTRIBUTE_ACTIVITY);
        Intent intent = activity.getIntent();

        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            String title = (String) intent.getSerializableExtra("title");
            if (title != null) {
                Log.d(NAME, "[processAction] " + title);
                activity.setTitle(title);
            }
        }
    }
}
