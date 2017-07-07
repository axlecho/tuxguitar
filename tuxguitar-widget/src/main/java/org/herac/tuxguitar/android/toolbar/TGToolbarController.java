package org.herac.tuxguitar.android.toolbar;

import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGToolbarController {

    private TGToolbar view;

    public TGToolbarController() {
        super();
    }

    public TGToolbar getView() {
        return view;
    }

    public void setView(TGToolbar view) {
        this.view = view;
    }

    public static TGToolbarController getInstance(TGContext context) {
        return TGSingletonUtil.getInstance(context, TGToolbarController.class.getName(), new TGSingletonFactory<TGToolbarController>() {
            public TGToolbarController createInstance(TGContext context) {
                return new TGToolbarController();
            }
        });
    }
}
