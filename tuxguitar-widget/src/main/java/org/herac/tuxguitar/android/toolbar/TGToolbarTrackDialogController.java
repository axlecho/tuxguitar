package org.herac.tuxguitar.android.toolbar;

import android.app.Activity;

import org.herac.tuxguitar.android.view.dialog.TGDialogContext;
import org.herac.tuxguitar.android.view.dialog.TGDialogController;
import org.herac.tuxguitar.android.view.dialog.TGDialogUtil;

public class TGToolbarTrackDialogController implements TGDialogController {

    @Override
    public void showDialog(Activity activity, TGDialogContext context) {
        TGDialogUtil.showDialog(activity, new TGToolbarTrackDialog(), context);
    }
}
