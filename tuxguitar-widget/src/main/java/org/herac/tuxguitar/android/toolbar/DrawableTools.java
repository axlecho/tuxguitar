package org.herac.tuxguitar.android.toolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;


/**
 * Created by mz on 6/17/16.
 */
public class DrawableTools {


    public static Drawable getTinyDrawable(Drawable drawable, ColorStateList colorStateList) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTintList(wrappedDrawable, colorStateList);
        return wrappedDrawable;
    }

    public static Drawable getTinyDrawable(Context context, @DrawableRes int drawableRes, int colorStateListRes) {
        Drawable drawable = getDrawable(context,drawableRes);
        ColorStateList colorStateList = ContextCompat.getColorStateList(context,colorStateListRes);
        return getTinyDrawable(drawable, colorStateList);
    }


    public static Drawable getDrawable(Context context, @DrawableRes int drawableRes) {
        Drawable drawable = ContextCompat.getDrawable(context,drawableRes);
        return drawable;
    }

}
