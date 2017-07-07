package org.herac.tuxguitar.android.view.tablature;

import org.herac.tuxguitar.android.action.impl.caret.TGMoveToAxisPositionAction;
import org.herac.tuxguitar.android.action.impl.gui.TGFullScreenAction;
import org.herac.tuxguitar.android.activity.TGActivityController;
import org.herac.tuxguitar.android.application.TGApplicationUtil;
import org.herac.tuxguitar.editor.action.TGActionProcessor;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Scroller;

public class TGSongViewGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private static final String TAG = "TGGesture";

    private GestureDetectorCompat gestureDetector;
    private TGSongViewScaleGestureDetector songViewScaleGestureDetector;
    private TGSongView songView;
    private TGScroller scroller;

    public TGSongViewGestureDetector(Context context, TGSongView songView) {
        this.gestureDetector = new GestureDetectorCompat(context, this);
        this.gestureDetector.setOnDoubleTapListener(this);
        this.songViewScaleGestureDetector = new TGSongViewScaleGestureDetector(context, songView);
        this.songView = songView;
        this.scroller = new TGScroller(context);
    }

    public boolean processTouchEvent(MotionEvent event) {
        this.songViewScaleGestureDetector.processTouchEvent(event);
        if (!this.songViewScaleGestureDetector.isInProgress()) {
            this.gestureDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        this.moveToAxisPosition(e.getX(), e.getY());
        return true;
    }

    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (this.songView.getController().isScrollActionAvailable()) {
            this.updateAxis(this.songView.getController().getScroll().getX(), distanceX);
            this.updateAxis(this.songView.getController().getScroll().getY(), distanceY);
        }
        return true;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // int distance = e2.getY() > e1.getY() ? -200 : 200;
        // this.scroller.startScroll(0,0,0,distance);
        int vx = (int) -velocityX;
        int vy = (int) -velocityY;

        this.scroller.fling(0, 0, vx, vy, -Integer.MAX_VALUE, Integer.MAX_VALUE, -Integer.MAX_VALUE, Integer.MAX_VALUE);
        this.songView.invalidate();
        return true;
    }

    public void updateAxis(TGScrollAxis axis, float distance) {
        if (axis.isEnabled()) {
            axis.setValue(Math.max(Math.min(axis.getValue() + distance, axis.getMaximum()), axis.getMinimum()));
        }
    }

    private void moveToAxisPosition(Float x, Float y) {
        TGActionProcessor tgActionProcessor = new TGActionProcessor(TGApplicationUtil.findContext(this.songView), TGMoveToAxisPositionAction.NAME);
        tgActionProcessor.setAttribute(TGMoveToAxisPositionAction.ATTRIBUTE_X, x);
        tgActionProcessor.setAttribute(TGMoveToAxisPositionAction.ATTRIBUTE_Y, y);
        tgActionProcessor.processOnNewThread();
    }

    public TGScroller getScroller() {
        return scroller;
    }
}
