package com.hopscotch.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hopscotch.android.R;
import com.hopscotch.android.core.HPApplication;
import com.hopscotch.android.core.events.HPLocationEvent;
import com.hopscotch.android.ui.fragment.ProfileFragment;
import com.hopscotch.android.ui.fragment.RouteListFragment;

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
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		//add profile fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, new ProfileFragment()).commit();

		Location location = HPApplication.getModel().getLocation();
		if (location == null) {
			Toast.makeText(this, "Location not found!", Toast.LENGTH_LONG).show();
		} else {

			LatLng coordinates = new LatLng(location.getLatitude(), location.getLongitude());

			map.getUiSettings().setZoomControlsEnabled(false);
			map.getUiSettings().setCompassEnabled(false);
			map.getUiSettings().setAllGesturesEnabled(false);
			map.getUiSettings().setMyLocationButtonEnabled(false);
			map.setMyLocationEnabled(true);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 13));

			map.addMarker(new MarkerOptions().title("You").position(coordinates));
		}

		//        Button hopButton = (Button) findViewById(R.id.hoponbutton);
		//        hopButton.setOnClickListener(new View.OnClickListener() {
		//            @Override
		//            public void onClick(View v) {
		//                hop();
		//            }
		//        });
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override public void onBackPressed() {
		if (getSupportFragmentManager().findFragmentById(R.id.content_fragment) instanceof RouteListFragment) {
			getSupportFragmentManager().popBackStack();
			return;
		}
		super.onBackPressed();
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.hop_item:
			doHop();
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	private void doHop() {
		getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, new RouteListFragment()).addToBackStack("").commit();
	}
}
