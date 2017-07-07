package org.herac.tuxguitar.android.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.herac.tuxguitar.android.R;

import org.herac.tuxguitar.android.action.TGActionProcessorListener;
import org.herac.tuxguitar.android.action.impl.device.TGSwitchOrientationAction;
import org.herac.tuxguitar.android.action.impl.gui.TGFinishAction;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.application.TGApplicationUtil;
import org.herac.tuxguitar.util.TGContext;

public class Titlebar extends RelativeLayout {
    public Titlebar(Context context) {
        super(context);
    }

    public Titlebar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Titlebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onFinishInflate() {
        this.attachView();
        this.addListeners();
        super.onFinishInflate();
    }

    public void attachView() {
        TitlebarController.getInstance(TGApplicationUtil.findContext(this)).setView(this);
    }

    public void addListeners() {
        findViewById(R.id.btn_back).setOnClickListener(createActionProcessor(TGFinishAction.NAME));
        findViewById(R.id.btn_rotate).setOnClickListener(createActionProcessor(TGSwitchOrientationAction.NAME));
    }

    public TGActionProcessorListener createActionProcessor(String actionId) {
        TGActionProcessorListener tgActionProcessor = new TGActionProcessorListener(findContext(), actionId);
        tgActionProcessor.setAttribute(TGSwitchOrientationAction.ATTRIBUTE_ACTIVITY, findActivity());
        return tgActionProcessor;
    }

    public TGActivity findActivity() {
        return (TGActivity) getContext();
    }

    public TGContext findContext() {
        return TGApplicationUtil.findContext(this);
    }
}
