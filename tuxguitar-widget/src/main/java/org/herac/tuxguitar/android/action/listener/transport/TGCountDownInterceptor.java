package org.herac.tuxguitar.android.action.listener.transport;

import android.content.Context;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.action.TGActionException;
import org.herac.tuxguitar.action.TGActionInterceptor;
import org.herac.tuxguitar.action.TGActionManager;
import org.herac.tuxguitar.android.action.impl.transport.TGCountDownAction;
import org.herac.tuxguitar.android.action.impl.transport.TGTransportPlayAction;
import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.android.persistence.TGPreferencesManager;
import org.herac.tuxguitar.editor.action.TGActionProcessor;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.util.TGContext;


public class TGCountDownInterceptor implements TGActionInterceptor {

    private static final String COUNTDOWN_ACTION_FINISH = "countdownInterceptor_confirmed";

    private TGContext context;

    public TGCountDownInterceptor(TGContext context) {
        this.context = context;
    }

    private boolean isActionConfirmed(TGActionContext context) {
        return Boolean.TRUE.equals(context.getAttribute(COUNTDOWN_ACTION_FINISH));
    }

    private boolean isTransportPlayAction(String id) {
        return (TGTransportPlayAction.NAME.equals(id));
    }

    @Override
    public boolean intercept(String id, TGActionContext context) throws TGActionException {
        if (this.isTransportPlayAction(id) && this.isCountDownEnable() &&
                !MidiPlayer.getInstance(this.context).isRunning() && !this.isActionConfirmed(context)) {
            this.processCountDown();
            return true;
        }

        return false;
    }

    public void processCountDown() {
        TGActionProcessor tgActionProcessor = new TGActionProcessor(this.context, TGCountDownAction.NAME);
        tgActionProcessor.setAttribute(TGCountDownAction.FINISH_ACTION, executeCountDownAction());
        tgActionProcessor.process();
    }

    public Runnable executeCountDownAction() {
        return new Runnable() {
            public void run() {
                processCountDownFinish();
            }
        };
    }

    public void processCountDownFinish() {
        TGActionProcessor tgActionProcessor = new TGActionProcessor(this.context, TGTransportPlayAction.NAME);
        tgActionProcessor.setAttribute(COUNTDOWN_ACTION_FINISH, true);
        tgActionProcessor.process();
    }

    public boolean isCountDownEnable() {
        return TGPreferencesManager.getInstance(this.context).isCountDownEnable(this.findContext());
    }

    public Context findContext() {
        return TGActivityController.getInstance(this.context).getActivity();
    }
}
