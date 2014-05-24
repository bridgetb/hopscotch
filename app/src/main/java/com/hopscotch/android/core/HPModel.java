package com.hopscotch.android.core;

import android.location.Location;

import com.hopscotch.android.core.events.HPGettingLocationEvent;
import com.hopscotch.android.core.events.HPLocationEvent;

public class HPModel {

	private Location mLocation;
	private boolean mGettingLocation = false;
    private boolean mBoarded = false;

	public Location getLocation() {
		return mLocation;
	}

	public void setLocation(Location location) {
		mLocation = location;
		HPApplication.dispatchEvent(new HPLocationEvent());
	}

	public void setGettingLocation(boolean value) {
		mGettingLocation = value;
		HPApplication.dispatchEvent(new HPGettingLocationEvent());
	}

	public boolean isGettingLocation() {
		return mGettingLocation;
	}

    public void setBoarded(boolean value) {
        mBoarded = value;
    }

    public boolean isBoarded() { return mBoarded; }

}
