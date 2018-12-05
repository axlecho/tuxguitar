package org.herac.tuxguitar.android.persistence;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.herac.tuxguitar.android.transport.TGPercent;
import org.herac.tuxguitar.graphics.control.TGLayout;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;


public class TGPreferencesManager {

    private static final String perference = "tgperference";
    private static final String TAG = "TGPreferencesManager";
    private static final int defaultStyle = TGLayout.DISPLAY_TABLATURE | TGLayout.DISPLAY_CHORD_NAME | TGLayout.DISPLAY_COMPACT | TGLayout.DISPLAY_CHORD_DIAGRAM;

    public int getSongViewDefaultStyle() {
        return defaultStyle;
    }

    public int getSongViewStyle(Context context) {
        SharedPreferences sp = context.getSharedPreferences(perference, Activity.MODE_PRIVATE);
        int style = sp.getInt("song_view_style", defaultStyle);
        Log.d(TAG, "[getSongViewStyle] style " + style);
        return style;
    }

    public void setSongViewStyle(Context context, int style) {
        Log.d(TAG, "[setSongViewStyle] style " + style);
        SharedPreferences sp = context.getSharedPreferences(perference, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("song_view_style", style);
        editor.apply();
    }

    public static TGPreferencesManager getInstance(TGContext context) {
        return TGSingletonUtil.getInstance(context, TGPreferencesManager.class.getName(), new TGSingletonFactory<TGPreferencesManager>() {
            public TGPreferencesManager createInstance(TGContext context) {
                return new TGPreferencesManager();
            }
        });
    }
}
