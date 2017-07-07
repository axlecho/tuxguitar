package org.herac.tuxguitar.android.view.popwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class TGPopwindowView extends View {
    public static final int SIZE = 256;
    private String count = "";
    private Paint paint;

    public TGPopwindowView(Context context, int count) {
        super(context);
        this.count = String.valueOf(count);
        this.init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextSize(48.0f);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(SIZE, SIZE);
    }

    @Override
    public void onDraw(Canvas canvas) {
//        paint.setColor(Color.WHITE);
//        paint.setAlpha(0xff);
//        canvas.drawRect(0,0,SIZE,SIZE,paint);

        paint.setColor(Color.BLACK);
        paint.setAlpha(0x33);
        canvas.drawCircle(SIZE / 2, SIZE / 2, SIZE / 2, paint);


        paint.setColor(Color.WHITE);
        paint.setAlpha(0xff);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (SIZE - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(count, SIZE / 2, baseline, paint);
    }
}
