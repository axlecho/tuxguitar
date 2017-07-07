package org.herac.tuxguitar.android.toolbar;

import android.util.Log;

import org.herac.tuxguitar.android.view.util.TGProcess;
import org.herac.tuxguitar.android.view.util.TGSyncProcessLocked;
import org.herac.tuxguitar.editor.event.TGUpdateEvent;
import org.herac.tuxguitar.event.TGEvent;
import org.herac.tuxguitar.event.TGEventListener;

public class TGToolbarTrackListListener implements TGEventListener {

    private TGToolbarTrackListAdapter adapter;
    private TGProcess updateSelection;
    private TGProcess updateTracks;

    public TGToolbarTrackListListener(TGToolbarTrackListAdapter adapter) {
        this.adapter = adapter;
        this.createSyncProcesses();
    }

    public void createSyncProcesses() {
        this.updateSelection = new TGSyncProcessLocked(this.adapter.findContext(), new Runnable() {
            public void run() {
                TGToolbarTrackListListener.this.adapter.updateSelection();
            }
        });

        this.updateTracks = new TGSyncProcessLocked(this.adapter.findContext(), new Runnable() {
            public void run() {
                TGToolbarTrackListListener.this.adapter.updateTracks();
            }
        });
    }

    public void processUpdateEvent(TGEvent event) {
        int type = ((Integer) event.getAttribute(TGUpdateEvent.PROPERTY_UPDATE_MODE)).intValue();

        Log.d(TGToolbarTrackListListener.class.getSimpleName(),"[processUpdateEvent] type " + type);
        if (type == TGUpdateEvent.SELECTION) {
            this.updateSelection.process();
        }
        if (type == TGUpdateEvent.SONG_UPDATED) {
            this.updateTracks.process();
        } else if (type == TGUpdateEvent.SONG_LOADED) {
            this.updateTracks.process();
        }
    }

    public void processEvent(final TGEvent event) {
        if (TGUpdateEvent.EVENT_TYPE.equals(event.getEventType())) {
            this.processUpdateEvent(event);
        }
    }
}
