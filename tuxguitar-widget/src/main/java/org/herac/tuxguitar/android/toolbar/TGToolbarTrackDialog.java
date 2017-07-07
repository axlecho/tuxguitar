package org.herac.tuxguitar.android.toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.ListView;

import org.herac.tuxguitar.android.R;

import org.herac.tuxguitar.android.view.dialog.TGDialog;

public class TGToolbarTrackDialog extends TGDialog {
    @Override
    public Dialog onCreateDialog() {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.view_toolbar_tracks_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.toolbar_tracks_dlg_title);
        installTrackListView(view);
        builder.setView(view);
        return builder.create();
    }

    public void installTrackListView(View dlgView) {
        final TGToolbarTrackListAdapter trackListAdapter = new TGToolbarTrackListAdapter(this.findContext(), this.findActivity());
        ListView listView = (ListView) dlgView.findViewById(R.id.toolbar_track_items);
        listView.setAdapter(trackListAdapter);
        trackListAdapter.attachListeners();
    }
}
