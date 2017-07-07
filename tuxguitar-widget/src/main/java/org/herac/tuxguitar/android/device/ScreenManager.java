package org.herac.tuxguitar.android.device;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.WindowManager;


import org.herac.tuxguitar.android.activity.TGActivity;

public class ScreenManager {

    private static final String TAG = "ScreenManager";
    private static ScreenManager mInstance;
    private Context mContext;

    private ScreenManager(Context context) {
        mContext = context;
    }

    public static ScreenManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (ScreenManager.class) {
                if (mInstance == null) {
                    mInstance = new ScreenManager(context);
                }
            }
        }
        return mInstance;
    }

    public void switchOrientation(Activity activity) {
        Log.d(TAG, "[switchOrientation]");
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void keepBrightness(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public void swithFullScreen(TGActivity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        if ((params.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) > 0) {
            cancelFullScreen(activity);
        } else {
            setFullScreen(activity);
        }
    }

    public void setFullScreen(TGActivity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(params);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

    }

    public void cancelFullScreen(TGActivity activity) {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(params);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
