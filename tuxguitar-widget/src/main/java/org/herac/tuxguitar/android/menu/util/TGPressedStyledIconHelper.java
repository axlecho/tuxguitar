package org.herac.tuxguitar.android.menu.util;

import org.herac.tuxguitar.util.TGContext;

public class TGPressedStyledIconHelper extends TGToggleStyledIconHelper {
    public TGPressedStyledIconHelper(TGContext context) {
        super(context);
    }

    @Override
    public void updateIcon(TGToggleStyledIconHandler handler) {

        if (this.button != null && handler != null) {
            Integer style = handler.resolveStyle();
            if (style != null) {
                button.setPressed(style == 1);
            }
        }
    }
}
