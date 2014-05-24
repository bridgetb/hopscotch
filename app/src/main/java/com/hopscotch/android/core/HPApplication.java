package com.hopscotch.android.core;

import android.app.Application;

import com.hopscotch.android.core.events.HPEvent;
import com.hopscotch.android.server.FetchUserLocationIntentService;

import de.greenrobot.event.EventBus;

public class HPApplication extends Application {

	private static HPApplication sInstance;

	private static EventBus sEventBus;

	private static HPModel mModel;

	public HPApplication() {
		sInstance = this;
	}

	public static HPApplication getInstance() {
		return sInstance;
	}

	public static HPModel getModel() {
		if (null == mModel) mModel = new HPModel();
		return mModel;
	}

	public static void dispatchEvent(HPEvent eventType) {
		HPApplication.getInstance().getEventBus().post(eventType);
	}

	public static EventBus getEventBus() {
		if (null == sEventBus) sEventBus = new EventBus();
		return sEventBus;
	}

	@Override public void onCreate() {
		super.onCreate();

		//get the users location
		FetchUserLocationIntentService.startAction(this);
	}
}
