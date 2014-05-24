package com.hopscotch.android.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.hopscotch.android.core.HPApplication;

public class FetchUserLocationIntentService extends Service implements GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	protected Context mContext;

	protected int mLocationRequestPriority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

	protected LocationClient mLocationClient;
	private HandlerThread mHandlerThread;
	private Looper mLooper;

	public static void startAction(Context context) {
		Intent intent = new Intent(context, FetchUserLocationIntentService.class);
		context.startService(intent);
	}

	public FetchUserLocationIntentService() {
		super();
	}

	@Override public IBinder onBind(Intent intent) {
		return null;
	}

	@Override public int onStartCommand(Intent intent, int flags, int startId) {
		handleAction();
		return Service.START_NOT_STICKY;
	}

	private void handleAction() {
		mContext = this;

		//connect location service
		mLocationClient = new LocationClient(mContext, this, this);
		mLocationClient.connect();
	}

	@Override public void onConnected(Bundle bundle) {

		long currentTime = System.currentTimeMillis();

		Location lastLocation = mLocationClient.getLastLocation();

		//if accuracy is less that 200 and the time is less than 1 minute
		if (null != lastLocation && lastLocation.getAccuracy() < 200 && (currentTime - lastLocation.getTime() < 60000)) {
			returnLocation(lastLocation);
			return;
		}

		//request updates to the location
		LocationRequest locationRequest = new LocationRequest();
		locationRequest.setExpirationDuration(60000);
		locationRequest.setPriority(mLocationRequestPriority);

		mHandlerThread = new HandlerThread("MyHandlerThread");
		mHandlerThread.start();
		mLooper = mHandlerThread.getLooper();

		mLocationClient.requestLocationUpdates(locationRequest, this, mLooper);
	}

	@Override public void onDestroy() {
		super.onDestroy();
		Log.d("HP", "onDestroy");
		finish();
	}

	@Override public void onDisconnected() {
		finish();
	}

	@Override public void onConnectionFailed(ConnectionResult connectionResult) {
		finish();
	}

	@Override public void onLocationChanged(Location location) {

		if (location.getAccuracy() < 200) {
			returnLocation(location);
		}
	}

	private void returnLocation(Location location) {
		HPApplication.getModel().setLocation(location);
		finish();
	}

	private void finish() {
		if (null != mHandlerThread && mHandlerThread.isAlive()) mHandlerThread.quit();
		if (null != mLocationClient && mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
			mLocationClient.disconnect();
		}
	}

}
