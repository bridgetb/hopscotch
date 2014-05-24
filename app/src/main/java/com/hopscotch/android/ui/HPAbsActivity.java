package com.hopscotch.android.ui;

import android.support.v7.app.ActionBarActivity;

import com.hopscotch.android.core.HPApplication;
import com.hopscotch.android.core.events.HPEvent;

/**
 * base activity class
 * 
 * registers to the event bus
 */
public abstract class HPAbsActivity extends ActionBarActivity {

	@Override protected void onResume() {
		super.onResume();
		HPApplication.getEventBus().register(this);
	}

	@Override protected void onPause() {
		super.onPause();
		HPApplication.getEventBus().unregister(this);
	}

	public void onEvent(HPEvent event) {

	}
}
