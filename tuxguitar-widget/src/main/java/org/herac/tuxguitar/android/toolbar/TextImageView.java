package org.herac.tuxguitar.android.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

import org.herac.tuxguitar.android.R;

public class TextImageView extends AppCompatTextView implements ViewTreeObserver.OnGlobalLayoutListener {
    private int icon = -1;
    private int icon_size = -1;
    private int icon_color = -1;

    public TextImageView(Context context) {
        super(context);
    }

    public TextImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initAttr(attrs);
    }

    public TextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initAttr(attrs);
    }

    public void initAttr(AttributeSet attrs) {
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.TextImageView);
        this.icon = t.getResourceId(R.styleable.TextImageView_src, R.drawable.ic_home);
        this.icon_size = t.getDimensionPixelSize(R.styleable.TextImageView_src_size, -1);
        this.icon_color = t.getResourceId(R.styleable.TextImageView_src_color, R.color.tab_home_selector);
        t.recycle();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void setSrc(int resId) {
        this.icon = resId;
        this.initIcon();
    }

    @Override
    public void onGlobalLayout() {
        this.initIcon();
    }

    private void initIcon() {
        Drawable drawable = DrawableTools.getTinyDrawable(this.getContext(), icon, icon_color);
        if (icon_size == -1) {
            this.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        } else {
            drawable.setBounds(0, 0, icon_size, icon_size);
            this.setCompoundDrawables(null, drawable, null, null);
        }
        this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

}
