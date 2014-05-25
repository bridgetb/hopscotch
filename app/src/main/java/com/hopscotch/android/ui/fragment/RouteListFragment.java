package com.hopscotch.android.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atapiwrapper.library.api.AtApi;
import com.atapiwrapper.library.api.model.ServerResponse;
import com.atapiwrapper.library.api.model.realtime.vehiclelocations.VehicleLocation;
import com.atapiwrapper.library.api.model.realtime.vehiclelocations.VehicleLocationResponse;
import com.hopscotch.android.BuildConfig;
import com.hopscotch.android.HPPointsActivity;
import com.hopscotch.android.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RouteListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<VehicleLocation>> {

	public RouteListFragment() {}

	private VehicleLocationAdapter mAdapter;
	private List<VehicleLocation> mVehicles = new ArrayList<>();

	private ProgressBar mProgressBar;

	@Override public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mAdapter = new VehicleLocationAdapter(getActivity(), mVehicles);
		getListView().setAdapter(mAdapter);

		mProgressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

		setProgressVisible(true);
		getLoaderManager().initLoader(0, null, this);
		getLoaderManager().getLoader(0).forceLoad();
	}

	@Override public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		HPPointsActivity.startActivity(getActivity());
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.route_list_fragment, container, false);
		return rootView;
	}

	public static class VehicleLocationAdapter extends ArrayAdapter<VehicleLocation> {

		List<Integer> colors = Arrays.asList(new Integer[] { R.color.text_blue, R.color.text_green, R.color.text_orange });

		public VehicleLocationAdapter(Context context, List<VehicleLocation> objects) {
			super(context, 0, objects);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			VehicleLocationWrapper vh = VehicleLocationWrapper.get(convertView, parent);
			VehicleLocation item = getItem(position);

			Collections.shuffle(colors, new Random());//get random color

			vh.id.setText(item.getVehicle().getTrip().getRouteId().substring(1, 4));
			vh.id.setTextColor(getContext().getResources().getColor(colors.get(0)));

			return vh.root;
		}

		public static class VehicleLocationWrapper {
			public static VehicleLocationWrapper get(View convertView, ViewGroup parent) {
				if (convertView == null) {
					return new VehicleLocationWrapper(parent);
				}
				return (VehicleLocationWrapper) convertView.getTag();
			}

			public final View root;

			public final TextView id;
			public final TextView description;

			private VehicleLocationWrapper(ViewGroup parent) {
				root = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_row, parent, false);
				root.setTag(this);
				id = (TextView) root.findViewById(R.id.route_id_textview);
				description = (TextView) root.findViewById(R.id.route_description_textview);
			}
		}
	}

	@Override public Loader onCreateLoader(int id, Bundle args) {
		return new VehicleLoader(getActivity());
	}

	@Override public void onLoadFinished(Loader<List<VehicleLocation>> loader, List<VehicleLocation> data) {
		mVehicles.clear();
		mVehicles.addAll(data);
		mAdapter.notifyDataSetChanged();
		setProgressVisible(false);
	}

	@Override public void onLoaderReset(Loader loader) {

	}

	private static class VehicleLoader extends AsyncTaskLoader<List<VehicleLocation>> {
		private VehicleLoader(Context context) {
			super(context);
		}

		@Override public List<VehicleLocation> loadInBackground() {

			AtApi api = new AtApi(BuildConfig.API_KEY);
			ServerResponse<VehicleLocationResponse> vehicles = api.getRealtimeService().vehicleLocations();
			if (null != vehicles && null != vehicles.getResponse()) return vehicles.getResponse().getVehicleLocations();
			return null;
		}
	}

	private void setProgressVisible(boolean value) {
		mProgressBar.setVisibility(value ? View.VISIBLE : View.GONE);
		getListView().setVisibility(value ? View.INVISIBLE : View.VISIBLE);
	}
}
