package org.herac.tuxguitar.android.action.impl.transport;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.TimerTask;

public class TGCountDownTask extends TimerTask {
    private int total;
    private CountDownListener listener;
    private CountDownHandler handler;

    public TGCountDownTask(int time, CountDownListener listener) {
        this.total = time;
        this.listener = listener;
    }

    public void attachToMainLoop(Looper looper) {
        handler = new CountDownHandler(looper);
    }

    public void dettachLoop() {
        handler = null;
    }

    @Override
    public void run() {
        handler.sendEmptyMessage(1);
    }

    public class CountDownHandler extends Handler {

        public CountDownHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (total < 0) {
                        listener.error();
                        break;
                    }

                    if (total == 0) {
                        listener.finish();
                        break;
                    }

                    listener.count(total);
                    total --;
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    }

    public interface CountDownListener {
        void count(int time);

        void finish();

        void error();
    }
}
