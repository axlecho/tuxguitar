package org.herac.tuxguitar.android.action.impl.transport;

import android.content.Context;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.android.view.popwindow.TGPopwindowController;
import org.herac.tuxguitar.android.view.popwindow.TGPopwindowView;
import org.herac.tuxguitar.util.TGContext;

import java.util.Timer;

public class TGCountDownAction extends TGActionBase implements TGCountDownTask.CountDownListener {

    public static final String NAME = "action.transport.countdown";
    public static final String FINISH_ACTION = "action.transport.countdown.finish";
    public static final int DURATION = 1000;
    public static final int TIME = 3;
    private Runnable finishAction;
    private Timer timer;
    private TGCountDownTask task;

    public TGCountDownAction(TGContext context) {
        super(context, NAME);
        android.util.Log.d("axlecho", "new action");
    }

    protected void processAction(TGActionContext context) {
        this.finishAction = context.getAttribute(FINISH_ACTION);
        this.initTimer();
        this.startTimer();
    }

    private void initTimer() {
        this.resetTimer();
        this.timer = new Timer();
        this.task = new TGCountDownTask(TIME, this);
        this.task.attachToMainLoop(findContext().getMainLooper());
    }

    private void startTimer() {
        timer.schedule(task, 0, DURATION);
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void resetTimer() {
        if (timer != null) {
            this.stopTimer();
            this.timer = null;
        }
    }

    @Override
    public void count(int time) {
        android.util.Log.d("axlecho", "time " + time);
        TGPopwindowController.getInstance(getContext()).setView(new TGPopwindowView(findContext(), time));
        TGPopwindowController.getInstance(getContext()).show();
    }

    @Override
    public void finish() {
        android.util.Log.d("axlecho", "finish");
        this.stopTimer();
        this.finishAction.run();
        this.task.dettachLoop();
        TGPopwindowController.getInstance(getContext()).dismiss();
    }

    @Override
    public void error() {
        android.util.Log.d("axlecho", "timer is crazy..");
        this.stopTimer();
        TGPopwindowController.getInstance(getContext()).dismiss();
    }

    public TGActivity findContext() {
        return TGActivityController.getInstance(this.getContext()).getActivity();
    }
}
