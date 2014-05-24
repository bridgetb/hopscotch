package com.hopscotch.android.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.hopscotch.android.R;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class SplashscreenActivity extends FragmentActivity implements ISimpleDialogListener {

	/* - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * start activity
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - */

	public static final int DIALOG_ID_PLAY_SERVICES_RESULT_ERROR = 101;
	public static final int PLAY_SERVICES_REQUEST_CODE = 999;
	public static final int PLAY_SERVICES_INVALID = 998;
	public static final String PLAY_SERVICES_ERROR_DIALOG_TAG = "playServicesError";

	private Handler mHandler = new Handler();
	private Runnable mRunnable;

	public static Intent startActivity(Activity activity) {
		Intent intent = new Intent(activity, SplashscreenActivity.class);
		activity.startActivity(intent);
		return intent;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen_activity);

		/* runnable that carries on to the main activity */
		mRunnable = new Runnable() {
			@Override public void run() {
				MainActivity.startActivity(SplashscreenActivity.this);
				finish();
			}
		};
	}

	@Override public void onResume() {
		super.onResume();
		//if Google Play Services exists start main activity. If it does't exist, the activity will handle it
		if (checkGooglePlayServices()) mHandler.postDelayed(mRunnable, 1000);
	}

	public void onPause() {
		super.onPause();
		/* stop runnable from posting if paused*/
		mHandler.removeCallbacks(mRunnable);
	};

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case PLAY_SERVICES_INVALID:
			this.finish();
			break;
		case PLAY_SERVICES_REQUEST_CODE:
			int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
			/* Google play services is still not installed. Display message and exit the app. */
			if (result != ConnectionResult.SUCCESS) finishWithPlayServicesError(this);
			break;
		}
	}

	public boolean checkGooglePlayServices() {

		if (getSupportFragmentManager().findFragmentByTag(PLAY_SERVICES_ERROR_DIALOG_TAG) == null) {
			int playServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
			switch (playServicesAvailable) {
			case ConnectionResult.SERVICE_DISABLED:
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			case ConnectionResult.SERVICE_MISSING:
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(playServicesAvailable, this, PLAY_SERVICES_REQUEST_CODE);
				dialog.setCancelable(false);
				dialog.show();
				return false;
			case ConnectionResult.SERVICE_INVALID:
				finishWithPlayServicesError(this);
				return false;
			}
		}
		return true;
	}

	private void finishWithPlayServicesError(Context context) {
		SimpleDialogFragment.createBuilder(context, getSupportFragmentManager()).setTitle(getString(R.string.sorry))
				.setMessage(getString(R.string.install_google_play_message)).setCancelable(false)
				.setPositiveButtonText(android.R.string.ok).setRequestCode(DIALOG_ID_PLAY_SERVICES_RESULT_ERROR).show();
	}

	@Override public void onPositiveButtonClicked(int requestCode) {
		switch (requestCode) {
		case DIALOG_ID_PLAY_SERVICES_RESULT_ERROR:
			this.finish();
			break;
		}

	}

	@Override public void onNegativeButtonClicked(int i) {

	}

}
