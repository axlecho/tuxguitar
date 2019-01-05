package org.herac.tuxguitar.android.toolbar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import org.herac.tuxguitar.android.R;
import org.herac.tuxguitar.android.action.TGActionProcessorListener;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenDialogAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenMenuAction;
import org.herac.tuxguitar.android.action.impl.transport.TGEnableCountDownAction;
import org.herac.tuxguitar.android.action.impl.transport.TGEnableMetronomeAction;
import org.herac.tuxguitar.android.action.impl.transport.TGTransportPlayAction;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.android.application.TGApplicationUtil;
import org.herac.tuxguitar.android.menu.context.TGContextMenuController;
import org.herac.tuxguitar.android.menu.context.impl.TGSettingMenu;
import org.herac.tuxguitar.android.menu.util.TGPressedStyledIconHelper;
import org.herac.tuxguitar.android.menu.util.TGToggleStyledIconHandler;
import org.herac.tuxguitar.android.menu.util.TGToggleStyledIconHelper;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.util.TGContext;

public class TGToolbar extends RelativeLayout {
    private TGToggleStyledIconHelper transportStyledIconHelper;
    private TGToggleStyledIconHelper timerStyledIconHelper;
    private TGToggleStyledIconHelper metronomeStyledIconHelper;

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

        this.transportStyledIconHelper = new TGToggleStyledIconHelper(this.findContext());
        this.timerStyledIconHelper = new TGPressedStyledIconHelper(this.findContext());
        this.metronomeStyledIconHelper = new TGPressedStyledIconHelper(this.findContext());

        this.fillStyledIconHandlers();
        this.initialize();
        super.onFinishInflate();
    }

    public void attachView() {
        TGToolbarController.getInstance(TGApplicationUtil.findContext(this)).setView(this);
    }

    public void addListeners() {
        findViewById(R.id.toolbar_tracks).setOnClickListener(createDialogActionProcessor(new TGToolbarTrackDialogController()));
        findViewById(R.id.toolbar_playrate).setOnClickListener(createDialogActionProcessor(new TGToolbarPlayRateDialogController()));
        findViewById(R.id.toolbar_settings).setOnClickListener(createContextMenuActionListener(new TGSettingMenu(this.findActivity())));
        findViewById(R.id.toolbar_transport_play).setOnClickListener(createActionProcessor(TGTransportPlayAction.NAME));
        findViewById(R.id.toolbar_timer).setOnClickListener(createActionProcessor(TGEnableCountDownAction.NAME));
        findViewById(R.id.toolbar_metronome).setOnClickListener(createActionProcessor(TGEnableMetronomeAction.NAME ));
    }

    public void initialize() {
        this.transportStyledIconHelper.initialize(this.findActivity(), (TextImageView) this.findViewById(R.id.toolbar_transport_play));
        this.timerStyledIconHelper.initialize(this.findActivity(), (TextImageView) this.findViewById(R.id.toolbar_timer));
        this.metronomeStyledIconHelper.initialize(this.findActivity(), (TextImageView) this.findViewById(R.id.toolbar_metronome ));
    }

    public void fillStyledIconHandlers() {
        this.transportStyledIconHelper.addHandler(this.createTransportStyledIconTransportHandler());
        this.timerStyledIconHelper.addHandler(this.createTimerStyledIconTransportHandler());
        this.metronomeStyledIconHelper.addHandler(this.createMetronomeStyledIconTransportHandler());
    }

    public TGToggleStyledIconHandler createTransportStyledIconTransportHandler() {
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

    public TGToggleStyledIconHandler createTimerStyledIconTransportHandler() {
        return new TGToggleStyledIconHandler() {

            public Integer getMenuItemId() {
                return -1;
            }

            public Integer resolveStyle() {
                boolean enabled = MidiPlayer.getInstance(findContext()).getCountDown().isEnabled();
                return (enabled ? 1 : 0);
            }
        };
    }

    public TGToggleStyledIconHandler createMetronomeStyledIconTransportHandler() {
        return new TGToggleStyledIconHandler() {

            public Integer getMenuItemId() {
                return -1;
            }

            public Integer resolveStyle() {
                boolean enabled = MidiPlayer.getInstance(findContext()).isMetronomeEnabled();
                return (enabled ?1 :0);
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
