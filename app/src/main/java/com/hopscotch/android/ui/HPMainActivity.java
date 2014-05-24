package com.hopscotch.android.ui;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;

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

        // Get a handle to the Map Fragment
        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        Location location = HPApplication.getModel().getLocation();
        if (location == null) {
            Toast.makeText(this, "Location not found!", Toast.LENGTH_LONG).show();
        }
        LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));

        map.addMarker(new MarkerOptions()
                .title("You")
                .position(coordinates));

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
		}

		location = HPApplication.getModel().getLocation();
		if (null != location) {
			Toast.makeText(
					this,
					String.format("%s %s", "location updated ", Double.toString(location.getLatitude()),
							Double.toString(location.getLongitude())), Toast.LENGTH_LONG).show();
		}

        Button hopButton = (Button) findViewById(R.id.hoponbutton);
        hopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hop();
            }
        });
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
//        Intent myIntent = new Intent(this, HPMapActivity.class);
//        HPMainActivity.this.startActivity(myIntent);
	}

    private void hop() {
        final Button hopButton = (Button) findViewById(R.id.hoponbutton);
        boolean isBoarded = HPApplication.getModel().isBoarded();
        if (isBoarded) {
            hopButton.setText("HOP ON");
        } else {
            hopButton.setText("HOP OFF");
        }
        HPApplication.getModel().setBoarded(!isBoarded);
    }
}
