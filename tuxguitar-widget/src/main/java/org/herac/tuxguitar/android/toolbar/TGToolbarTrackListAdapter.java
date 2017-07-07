package org.herac.tuxguitar.android.toolbar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import org.herac.tuxguitar.android.R;

import org.herac.tuxguitar.android.action.TGActionProcessorListener;
import org.herac.tuxguitar.android.action.impl.track.TGGoToTrackAction;
import org.herac.tuxguitar.android.action.impl.track.TGMuteTrackAction;
import org.herac.tuxguitar.android.view.tablature.TGSongViewController;
import org.herac.tuxguitar.android.view.util.TGProcess;
import org.herac.tuxguitar.android.view.util.TGSyncProcessLocked;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.document.TGDocumentManager;
import org.herac.tuxguitar.editor.TGEditorManager;
import org.herac.tuxguitar.editor.action.TGActionProcessor;
import org.herac.tuxguitar.event.TGEventListener;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.util.TGContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TGToolbarTrackListAdapter extends BaseAdapter {

    private TGTrack selection;
    private TGEventListener eventListener;
    private List<TGToolbarTrackListItem> items;
    private TGContext context;
    private Activity activity;
    private TGProcess updateSelection;

    public TGToolbarTrackListAdapter(TGContext context, Activity activity) {
        this.context = context;
        this.activity = activity;
        this.items = new ArrayList<>();
        this.eventListener = new TGToolbarTrackListListener(this);
        this.createSyncProcesses();
        updateSelection.process();
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < this.items.size()) {
            return this.items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TGToolbarTrackListItem item = (TGToolbarTrackListItem) this.getItem(position);

        View view = (convertView != null ? convertView : getLayoutInflater().inflate(R.layout.view_main_drawer_check_item, parent, false));

        CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.main_drawer_check_item);
        checkedTextView.setText(item.getLabel());
        checkedTextView.setChecked(Boolean.TRUE.equals(item.getSelected()));
        checkedTextView.setOnClickListener(this.createGoToTrackAction(item.getTrack()));

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox_mute_track);
        checkBox.setChecked(Boolean.TRUE.equals(item.getTrack().isMute()));
        checkBox.setOnClickListener(this.createMuteTrackAction(item.getTrack()));
        return view;
    }

    public boolean isUpdateRequired() {
        android.util.Log.d("axlecho", "[isUpdateRequired]");
        TGSong song = TGDocumentManager.getInstance(findContext()).getSong();
        if (song != null) {
            int count = song.countTracks();
            if (count != this.getCount()) {
                return true;
            }
            for (int i = 0; i < count; i++) {
                TGTrack track = song.getTrack(i);
                TGToolbarTrackListItem item = (TGToolbarTrackListItem) this.getItem(i);
                if (track == null || item == null) {
                    return true;
                }

                // Order changed
                if (!track.equals(item.getTrack())) {
                    return true;
                }

                // Name changed
                if (!track.getName().equals(item.getLabel())) {
                    return true;
                }

                // Selection changed
                if (!Boolean.valueOf(this.isSelected(track)).equals(item.getSelected())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSelected(TGTrack track) {
        return (this.selection != null && track != null && this.selection.equals(track));
    }

    public void updateTrackItems() {
        this.items.clear();

        TGSong song = TGDocumentManager.getInstance(this.findContext()).getSong();
        if (song != null) {
            Iterator<TGTrack> tracks = song.getTracks();
            while (tracks.hasNext()) {
                TGTrack track = tracks.next();

                TGToolbarTrackListItem item = new TGToolbarTrackListItem();
                item.setTrack(track);
                item.setLabel(track.getName());
                item.setSelected(isSelected(track));

                this.items.add(item);
            }
        }
    }

    public void updateSelection() {
        this.selection = TGSongViewController.getInstance(this.findContext()).getCaret().getTrack();
        if (this.isUpdateRequired()) {
            this.updateTracks();
        }
    }

    public void updateTracks() {
        this.updateTrackItems();
        this.notifyDataSetChanged();
    }

    public void attachListeners() {
        TGEditorManager.getInstance(this.findContext()).addUpdateListener(this.eventListener);
    }

    public void detachListeners() {
        TGEditorManager.getInstance(this.findContext()).removeUpdateListener(this.eventListener);
    }

    public LayoutInflater getLayoutInflater() {
        return (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public TGContext findContext() {
        return context;
    }

    public TGActionProcessorListener createGoToTrackAction(TGTrack track) {
        TGActionProcessorListener tgActionProcessor = this.createAction(TGGoToTrackAction.NAME);
        tgActionProcessor.setAttribute(TGDocumentContextAttributes.ATTRIBUTE_TRACK, track);
        return tgActionProcessor;
    }

    public TGActionProcessorListener createMuteTrackAction(TGTrack track) {
        TGActionProcessorListener tgActionProcessor = this.createAction(TGMuteTrackAction.NAME);
        tgActionProcessor.setAttribute(TGDocumentContextAttributes.ATTRIBUTE_TRACK, track);
        tgActionProcessor.setAttribute(TGMuteTrackAction.ATTRIBUTE_MUTE, !track.isMute());
        return tgActionProcessor;
    }

    public TGActionProcessorListener createAction(String actionId) {
        return new TGActionProcessorListener(this.findContext(), actionId);
    }

    public void createSyncProcesses() {
        this.updateSelection = new TGSyncProcessLocked(this.findContext(), new Runnable() {
            public void run() {
                TGToolbarTrackListAdapter.this.updateSelection();
            }
        });
    }
}
