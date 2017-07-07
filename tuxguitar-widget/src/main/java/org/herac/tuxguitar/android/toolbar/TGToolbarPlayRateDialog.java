package org.herac.tuxguitar.android.toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.herac.tuxguitar.android.R;

import org.herac.tuxguitar.android.action.impl.transport.TGSetPlayRateAction;
import org.herac.tuxguitar.android.transport.TGPercent;
import org.herac.tuxguitar.android.transport.TGTransport;
import org.herac.tuxguitar.android.view.dialog.TGDialog;
import org.herac.tuxguitar.editor.action.TGActionProcessor;

import java.util.ArrayList;
import java.util.List;

public class TGToolbarPlayRateDialog extends TGDialog {

    private List<TGToolbarPlayRateListItem> tgPlayRateList;
    private SeekBar seekBar;
    private TextView textView;

    @SuppressLint("InflateParams")
    public Dialog onCreateDialog() {
        tgPlayRateList = generatePercent();

        final View view = getActivity().getLayoutInflater().inflate(R.layout.view_toolbar_playrate_dialog, null);
        this.seekBar = (SeekBar) view.findViewById(R.id.toolbar_percent_seekbar);
        this.textView = (TextView) view.findViewById(R.id.toolbar_percent_textview);

        this.seekBar.setMax(tgPlayRateList.size() - 1);
        this.initPlayRate();
        this.appendListeners(seekBar);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.toolbar_playrate_dlg_title);
        builder.setView(view);
        return builder.create();
    }

    public List<TGToolbarPlayRateListItem> generatePercent() {
        List<TGToolbarPlayRateListItem> percentList = new ArrayList<>();
        List<Integer> percents = TGPercent.getInstance(findContext()).getPercentList();
        for (Integer percent : percents) {
            percentList.add(new TGToolbarPlayRateListItem(percent));
        }
        return percentList;
    }

    public void appendListeners(final SeekBar seekBar) {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                setPlayRate(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setPlayRate(int pos) {
        this.textView.setText(tgPlayRateList.get(pos).toString());
        TGActionProcessor tgActionProcessor = new TGActionProcessor(findContext(), TGSetPlayRateAction.NAME);
        tgActionProcessor.setAttribute(TGSetPlayRateAction.ATTRIBUTE_PERCENT, tgPlayRateList.get(pos).rate);
        tgActionProcessor.processOnCurrentThread();
    }

    public void initPlayRate() {
        int current = TGTransport.getInstance(findContext()).getPercent();
        for (int i = 0; i < tgPlayRateList.size(); i++) {
            TGToolbarPlayRateListItem item = tgPlayRateList.get(i);
            if (current == item.rate) {
                this.seekBar.setProgress(i);
                this.textView.setText(item.toString());
                return;
            }
        }

    }
}
