package org.herac.tuxguitar.android.action.impl.transport;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.TuxGuitar;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.util.TGContext;

public class TGEnableMetronomeAction extends TGActionBase {
    public static final String NAME = "action.transport.metronome-enable";

    public TGEnableMetronomeAction(TGContext context) {
        super(context, NAME);
    }

    @Override
    protected void processAction(TGActionContext context) {
        MidiPlayer player = TuxGuitar.getInstance(getContext()).getPlayer();
        player.setMetronomeEnabled(!player.isMetronomeEnabled());
    }
}
