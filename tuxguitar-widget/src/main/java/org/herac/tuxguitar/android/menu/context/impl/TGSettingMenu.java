package org.herac.tuxguitar.android.menu.context.impl;

import android.view.ContextMenu;
import android.view.MenuInflater;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.TuxGuitar;
import org.herac.tuxguitar.android.action.impl.layout.TGSetChordDiagramEnabledAction;
import org.herac.tuxguitar.android.action.impl.layout.TGSetChordNameEnabledAction;
import org.herac.tuxguitar.android.action.impl.layout.TGSetScoreEnabledAction;
import org.herac.tuxguitar.android.action.impl.transport.TGEnableCountDownAction;
import org.herac.tuxguitar.android.action.impl.transport.TGEnableMetronomeAction;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.menu.context.TGContextMenuBase;
import org.herac.tuxguitar.android.view.tablature.TGSongViewController;
import org.herac.tuxguitar.graphics.control.TGLayout;
import org.herac.tuxguitar.util.TGContext;

public class TGSettingMenu extends TGContextMenuBase {

    public TGSettingMenu(TGActivity activity) {
        super(activity);
    }

    public void inflate(ContextMenu menu, MenuInflater inflater) {
        menu.setHeaderTitle(R.string.toolbar_view);
        inflater.inflate(R.menu.menu_view, menu);
        initializeItems(menu);
    }

    public void initializeItems(ContextMenu menu) {
        TGContext context = findContext();
        TGLayout layout = TGSongViewController.getInstance(context).getLayout();
        int style = layout.getStyle();

        this.initializeItem(menu, R.id.menu_view_layout_show_score, this.createActionProcessor(TGSetScoreEnabledAction.NAME), true, (style & TGLayout.DISPLAY_SCORE) != 0);
        this.initializeItem(menu, R.id.menu_view_layout_show_chord_name, this.createActionProcessor(TGSetChordNameEnabledAction.NAME), true, (style & TGLayout.DISPLAY_CHORD_NAME) != 0);
        this.initializeItem(menu, R.id.menu_view_layout_show_chord_diagram, this.createActionProcessor(TGSetChordDiagramEnabledAction.NAME), true, (style & TGLayout.DISPLAY_CHORD_DIAGRAM) != 0);
        this.initializeItem(menu, R.id.menu_countdown_enable, this.createActionProcessor(TGEnableCountDownAction.NAME), true, TuxGuitar.getInstance(context).getPlayer().getCountDown().isEnabled());
        this.initializeItem(menu, R.id.menu_metronome, this.createActionProcessor(TGEnableMetronomeAction.NAME), true, TuxGuitar.getInstance(context).getPlayer().isMetronomeEnabled());
    }
}
