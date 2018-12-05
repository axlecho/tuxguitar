package org.herac.tuxguitar.android.action.impl.transport;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.TuxGuitar;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.player.base.MidiPlayerCountDown;
import org.herac.tuxguitar.util.TGContext;

public class TGEnableCountDownAction extends TGActionBase {
    public static final String NAME = "action.transport.countdown-enable";
    public TGEnableCountDownAction(TGContext context) {
        super(context, NAME);
    }

    @Override
    protected void processAction(TGActionContext context) {
        MidiPlayerCountDown countDown = TuxGuitar.getInstance(getContext()).getPlayer().getCountDown();
        countDown.setEnabled(!countDown.isEnabled());
    }
}
