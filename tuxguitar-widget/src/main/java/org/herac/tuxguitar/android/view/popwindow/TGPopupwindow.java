package org.herac.tuxguitar.android.view.popwindow;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import org.herac.tuxguitar.android.activity.TGActivity;

public class TGPopupwindow extends PopupWindow {
    private TGActivity activity;

    public TGPopupwindow(TGActivity activity) {
        super(activity);
        this.activity = activity;
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new ColorDrawable(0));
    }

    public void popup() {
        android.util.Log.d("axlecho", "popup");
        this.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    public void setView(TGPopwindowView view) {
        android.util.Log.d("axlecho", "setview");
        this.setContentView(view);
    }
}
