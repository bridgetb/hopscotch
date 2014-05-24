package com.hopscotch.android.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hopscotch.android.R;
import com.hopscotch.android.core.HPApplication;
import com.hopscotch.android.core.events.HPLocationEvent;

public class HPMainActivity extends HPAbsActivity {

	public static Intent startActivity(Activity activity) {
		Intent intent = new Intent(activity, HPMainActivity.class);
		activity.startActivity(intent);
		return intent;
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

		Location location = HPApplication.getModel().getLocation();
		if (null != location) {
			Toast.makeText(
					this,
					String.format("%s %s", "location updated ", Double.toString(location.getLatitude()),
							Double.toString(location.getLongitude())), Toast.LENGTH_LONG).show();
		}
	}

	@Override protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {}

		@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.main_fragment, container, false);
			return rootView;
		}
	}

	public void onEvent(HPLocationEvent event) {
		Location location = HPApplication.getModel().getLocation();
		if (null == location) return;

		Toast.makeText(
				this,
				String.format("%s %s", "location updated ", Double.toString(location.getLatitude()),
						Double.toString(location.getLongitude())), Toast.LENGTH_LONG).show();
	}
}
