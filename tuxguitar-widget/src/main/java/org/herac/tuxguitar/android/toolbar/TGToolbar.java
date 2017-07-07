package org.herac.tuxguitar.android.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import org.herac.tuxguitar.android.R;

import org.herac.tuxguitar.android.action.TGActionProcessorListener;
import org.herac.tuxguitar.android.action.TGUiActionPrecessorListener;
import org.herac.tuxguitar.android.action.impl.gui.TGFullScreenAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenDialogAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenMenuAction;
import org.herac.tuxguitar.android.action.impl.transport.TGTransportPlayAction;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.application.TGApplicationUtil;
import org.herac.tuxguitar.android.menu.context.TGContextMenuController;
import org.herac.tuxguitar.android.menu.context.impl.TGSettingMenu;
import org.herac.tuxguitar.android.menu.util.TGToggleStyledIconHandler;
import org.herac.tuxguitar.android.menu.util.TGToggleStyledIconHelper;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.util.TGContext;

public class TGToolbar extends RelativeLayout {
    private TGToggleStyledIconHelper styledIconHelper;

    public TGToolbar(Context context) {
        super(context);
    }

    public TGToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TGToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void onFinishInflate() {
        this.attachView();
        this.addListeners();

        this.styledIconHelper = new TGToggleStyledIconHelper(this.findContext());
        this.fillStyledIconHandlers();
        this.initialize(this.findActivity(), (TextImageView) this.findViewById(R.id.toolbar_transport_play));
    }

    public void attachView() {
        TGToolbarController.getInstance(TGApplicationUtil.findContext(this)).setView(this);
    }

    public void addListeners() {
        findViewById(R.id.toolbar_tracks).setOnClickListener(createDialogActionProcessor(new TGToolbarTrackDialogController()));
        findViewById(R.id.toolbar_playrate).setOnClickListener(createDialogActionProcessor(new TGToolbarPlayRateDialogController()));
        findViewById(R.id.toolbar_settings).setOnClickListener(createContextMenuActionListener(new TGSettingMenu(this.findActivity())));
        findViewById(R.id.toolbar_transport_play).setOnClickListener(createActionProcessor(TGTransportPlayAction.NAME));
    }

    public void initialize(TGActivity activity, TextImageView button) {
        this.styledIconHelper.initialize(activity, button);
    }

    public void fillStyledIconHandlers() {
        this.styledIconHelper.addHandler(this.createStyledIconTransportHandler());
    }

    public TGToggleStyledIconHandler createStyledIconTransportHandler() {
        return new TGToggleStyledIconHandler() {

            public Integer getMenuItemId() {
                return -1;
            }

            public Integer resolveStyle() {
                boolean running = MidiPlayer.getInstance(findContext()).isRunning();
                return (running ? R.drawable.ic_pause : R.drawable.ic_play);
            }
        };
    }

    public TGActionProcessorListener createContextMenuActionListener(TGContextMenuController controller) {
        TGActionProcessorListener tgActionProcessor = new TGActionProcessorListener(this.findContext(), TGOpenMenuAction.NAME);
        tgActionProcessor.setAttribute(TGOpenMenuAction.ATTRIBUTE_MENU_CONTROLLER, controller);
        tgActionProcessor.setAttribute(TGOpenMenuAction.ATTRIBUTE_MENU_ACTIVITY, this.findActivity());
        return tgActionProcessor;
    }

    public TGActionProcessorListener createActionProcessor(String actionId) {
        TGActionProcessorListener tgActionProcessor = new TGActionProcessorListener(findContext(), actionId);
        tgActionProcessor.setAttribute(TGOpenMenuAction.ATTRIBUTE_MENU_ACTIVITY, findActivity());
        return tgActionProcessor;
    }

    public TGActionProcessorListener createDialogActionProcessor(TGDialogController controller) {
        TGActionProcessorListener tgActionProcessor = this.createActionProcessor(TGOpenDialogAction.NAME);
        tgActionProcessor.setAttribute(TGOpenDialogAction.ATTRIBUTE_DIALOG_CONTROLLER, controller);
        tgActionProcessor.setAttribute(TGOpenDialogAction.ATTRIBUTE_DIALOG_ACTIVITY, findActivity());
        return tgActionProcessor;
    }

    public TGActivity findActivity() {
        return (TGActivity) getContext();
    }

    public TGContext findContext() {
        return TGApplicationUtil.findContext(this);
    }

}
