package org.herac.tuxguitar.android.view.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import org.herac.tuxguitar.graphics.TGRectangle;

public class TGRenderHelper {
    private Bitmap bufferedBitmap;
    private Bitmap cleanBuffer;
    private Bitmap dirtyBuffer;

    private Rect clientArea;
    private int preX;
    private int preY;

    private int currentX;
    private int currentY;

    private boolean initFlag;

    public TGRenderHelper() {
        this.preX = 0;
        this.preY = 0;
    }
    public int getFixY() {
        if(cleanBuffer == null) {
            return currentY;
        }

        if(dirtyBuffer == null) {
            return currentY;
        }

        return isDown() ? currentY + cleanBuffer.getHeight(): currentY;
    }
    private boolean isDown() {
        return currentY - preY > 0;
    }

    public void preparePaint(Canvas canvas, int x, int y) {
        this.currentX = x;
        this.currentY = y;
        this.clientArea = canvas.getClipBounds();
        this.createBuffer();
        this.createCleanBuffer();
        this.createDirtyBuffer();
    }

    public Bitmap getDirtyBuffer() {
        return dirtyBuffer;
    }

    public TGRectangle getDirtyArea() {
        return new TGRectangle(0f,0f,dirtyBuffer.getWidth(),dirtyBuffer.getHeight());
    }

    public Bitmap getBuffer() {
        this.initFlag = false;
        if (dirtyBuffer == null) {
            return bufferedBitmap;
        }

        if (cleanBuffer == null) {
            mergeBitmap(dirtyBuffer);
            return bufferedBitmap;
        }


        if (isDown()) {
            // clean + dirty
            mergeBitmap(cleanBuffer, dirtyBuffer);
        } else {
            // dirty + clean
            mergeBitmap(dirtyBuffer, cleanBuffer);
        }
        this.preX = this.currentX;
        this.preY = this.currentY;
        return bufferedBitmap;
    }

    public void recycleBuffer() {
        if (this.bufferedBitmap != null && !this.bufferedBitmap.isRecycled()) {
            this.bufferedBitmap.recycle();
            this.bufferedBitmap = null;
        }

        if (this.cleanBuffer != null && !this.cleanBuffer.isRecycled()) {
            this.cleanBuffer.recycle();
            this.cleanBuffer = null;
        }

        if (this.dirtyBuffer != null && !this.dirtyBuffer.isRecycled()) {
            this.dirtyBuffer.recycle();
            this.dirtyBuffer = null;
        }
    }

    private void createBuffer() {
        if (this.bufferedBitmap == null || this.bufferedBitmap.getWidth() != clientArea.width() || this.bufferedBitmap.getHeight() != clientArea.height()) {
            this.recycleBuffer();
            this.bufferedBitmap = Bitmap.createBitmap(clientArea.width(), clientArea.height(), Bitmap.Config.RGB_565);
            this.initFlag = true;
        }
    }

    private Rect calculateDirtyArea() {
        Rect cleanArea = calculateCleanArea();
        return new Rect(0, 0, clientArea.width(), clientArea.height() - cleanArea.height());

    }

    private Rect calculateCleanArea() {
        if (initFlag) {
            return new Rect();
        }

        int height = clientArea.height() - Math.abs(currentY - preY);
        int y = isDown() ? Math.abs(currentY - preY) : 0;
        return new Rect(0, y, clientArea.width(), y + height);
    }

    private void createCleanBuffer() {
        Rect cleanArea = calculateCleanArea();
        Log.d("XXXXXXXXX", "clean - " + cleanArea.toShortString());
        if (cleanBuffer != null && !cleanBuffer.isRecycled()) {
            cleanBuffer.recycle();
            cleanBuffer = null;
        }

        // first without clean area
        if (cleanArea.isEmpty()) {
            return;
        }
        cleanBuffer = Bitmap.createBitmap(bufferedBitmap,cleanArea.left,cleanArea.top,cleanArea.width(),cleanArea.height());
    }

    private void createDirtyBuffer() {
        Rect dirtyArae = calculateDirtyArea();
        Log.d("XXXXXXXXX", "dirty - " + dirtyArae.toShortString());

        if (dirtyBuffer != null && !dirtyBuffer.isRecycled()) {
            dirtyBuffer.recycle();
            dirtyBuffer = null;
        }
        if (dirtyArae.width() <= 0 || dirtyArae.height() <= 0) {
            return;
        }
        dirtyBuffer = Bitmap.createBitmap(dirtyArae.width(), dirtyArae.height(), Bitmap.Config.RGB_565);
    }

    private void mergeBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        Canvas canvas = new Canvas(bufferedBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    private void mergeBitmap(Bitmap topBitmap, Bitmap bottomBitmap) {

        if (topBitmap == null || topBitmap.isRecycled() || bottomBitmap == null || bottomBitmap.isRecycled()) {
            return;
        }


        Canvas canvas = new Canvas(bufferedBitmap);
        canvas.drawBitmap(topBitmap, 0, 0, null);
        canvas.drawBitmap(bottomBitmap, 0, topBitmap.getHeight(), null);
    }
}
