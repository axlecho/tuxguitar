package org.herac.tuxguitar.android.view.tablature;

import android.util.Log;

import org.herac.tuxguitar.android.TuxGuitar;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.player.base.MidiPlayerMode;
import org.herac.tuxguitar.util.TGContext;

public class TGLoop {
    public static final int LOOP_AB = 1;
    public static final int LOOP_NONE = 0;
    public static final int LOOP_A = 2;

    private int status;
    private TGSongViewController tablature;

    public TGLoop(TGSongViewController tablature) {
        this.status = LOOP_NONE;
        this.tablature = tablature;

    }

    public int getStatus() {
        return status;
    }

    public void setLoop(TGMeasureImpl measure) {
        TGContext context = this.tablature.getContext();
        MidiPlayerMode pm = TuxGuitar.getInstance(context).getPlayer().getMode();
        if (status ==  LOOP_NONE) {
            pm.setLoop(false);
            pm.setLoopSHeader(measure.getHeader().getNumber());
            status = LOOP_A;
        } else if(status == LOOP_A) {
            pm.setLoop(true);
            pm.setLoopEHeader(measure.getHeader().getNumber());
            status = LOOP_AB;
            Log.d("play-loop","set loop from " + pm.getLoopSHeader() + " to " + pm.getLoopEHeader());
        } else if(status == LOOP_AB) {
            pm.setLoop(false);
            pm.setLoopSHeader(-1);
            pm.setLoopSHeader(-1);
            status = LOOP_NONE;
        }
    }
}
