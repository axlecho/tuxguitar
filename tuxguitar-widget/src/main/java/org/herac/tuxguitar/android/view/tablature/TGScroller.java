package org.herac.tuxguitar.android.view.tablature;


import android.content.Context;
import android.widget.Scroller;

public class TGScroller extends Scroller {
    private int lastX = 0;
    private int lastY = 0;

    public TGScroller(Context context) {
        super(context);
    }

    @Override
    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        lastX = 0;
        lastY = 0;
        super.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
    }

    public int getCurrDistanceX() {
        int d = this.getCurrX() - lastX;
        lastX = this.getCurrX();
        return d;
    }

    public int getCurrDistanceY() {
        int d = this.getCurrY() - lastY;
        lastY = this.getCurrY();
        return d;
    }
}
