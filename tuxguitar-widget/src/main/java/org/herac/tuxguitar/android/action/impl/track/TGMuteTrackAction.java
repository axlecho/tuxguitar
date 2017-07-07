package org.herac.tuxguitar.android.action.impl.track;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.android.action.TGActionBase;
import org.herac.tuxguitar.android.transport.TGTransport;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.util.TGContext;

public class TGMuteTrackAction extends TGActionBase {
    public static final String NAME = "action.track.mute";
    public static final String ATTRIBUTE_MUTE = "attribute.mute";

    public TGMuteTrackAction(TGContext context) {
        super(context, NAME);
    }

    protected void processAction(TGActionContext context) {
        TGTrack track = ((TGTrack) context.getAttribute(TGDocumentContextAttributes.ATTRIBUTE_TRACK));
        boolean mute = context.getAttribute(this.ATTRIBUTE_MUTE);

        android.util.Log.d("axlecho", "mute action " + mute);
        if (track != null) {
            track.setMute(mute);
            TGTransport.getInstance(getContext()).reset();
        }
    }
}
