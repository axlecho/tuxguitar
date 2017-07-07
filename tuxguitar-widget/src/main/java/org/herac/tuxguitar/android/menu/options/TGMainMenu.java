package org.herac.tuxguitar.android.menu.options;

import android.view.Menu;

import org.herac.tuxguitar.android.R;

import org.herac.tuxguitar.android.action.TGActionProcessorListener;
import org.herac.tuxguitar.android.action.impl.device.TGSwitchOrientationAction;
import org.herac.tuxguitar.android.action.impl.gui.TGOpenMenuAction;
import org.herac.tuxguitar.android.activity.TGActivity;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.singleton.TGSingletonFactory;
import org.herac.tuxguitar.util.singleton.TGSingletonUtil;

public class TGMainMenu {

	private TGContext context;
	private TGActivity activity;
	private Menu menu;

	private TGMainMenu(TGContext context) {
		this.context = context;
	}

	public void initialize(TGActivity activity, Menu menu) {
		this.activity = activity;
		this.menu = menu;
		this.initializeItems();
	}

	public TGContext getContext() {
		return context;
	}

	public TGActivity getActivity() {
		return activity;
	}

	public Menu getMenu() {
		return menu;
	}

	public void initializeItems() {
		this.getMenu().findItem(R.id.menu_switch_orientation).setOnMenuItemClickListener(createActionProcessor(TGSwitchOrientationAction.NAME));
	}

	public TGActionProcessorListener createActionProcessor(String actionId) {
		TGActionProcessorListener tgActionProcessor = new TGActionProcessorListener(getContext(), actionId);
		tgActionProcessor.setAttribute(TGOpenMenuAction.ATTRIBUTE_MENU_ACTIVITY, getActivity());
		return tgActionProcessor;
	}

	public static TGMainMenu getInstance(TGContext context) {
		return TGSingletonUtil.getInstance(context, TGMainMenu.class.getName(), new TGSingletonFactory<TGMainMenu>() {
			public TGMainMenu createInstance(TGContext context) {
				return new TGMainMenu(context);
			}
		});
	}
}
