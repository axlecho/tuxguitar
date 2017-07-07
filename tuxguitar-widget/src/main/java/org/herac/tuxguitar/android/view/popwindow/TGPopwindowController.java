package org.herac.tuxguitar.android.view.popwindow;

import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGPopwindowController {

    private TGPopupwindow window;
    private TGContext context;

    private TGPopwindowController(TGContext context) {
        this.context = context;
        this.window = new TGPopupwindow(findActivity());
    }

    public void setView(TGPopwindowView view) {
        this.dismiss();
        this.window.setView(view);
    }

    public void show() {
        this.dismiss();
        this.window.popup();
    }

    public void dismiss() {
        if (this.window.isShowing()) {
            this.window.dismiss();
        }
    }

    public static TGPopwindowController getInstance(TGContext context) {
        return TGSingletonUtil.getInstance(context, TGPopwindowController.class.getName(), new TGSingletonFactory<TGPopwindowController>() {
            public TGPopwindowController createInstance(TGContext context) {
                return new TGPopwindowController(context);
            }
        });
    }

    public TGActivity findActivity() {
        return TGActivityController.getInstance(context).getActivity();
    }
}
