package org.herac.tuxguitar.android.titlebar;

import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TitlebarController {
    private Titlebar view;

    public TitlebarController() {
        super();
    }

    public Titlebar getView() {
        return view;
    }

    public void setView(Titlebar view) {
        this.view = view;
    }

    public static TitlebarController getInstance(TGContext context) {
        return TGSingletonUtil.getInstance(context, TitlebarController.class.getName(), new TGSingletonFactory<TitlebarController>() {
            public TitlebarController createInstance(TGContext context) {
                return new TitlebarController();
            }
        });
    }
}
