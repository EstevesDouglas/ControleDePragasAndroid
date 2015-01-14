package com.example.controlepragas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.controlepragas.service.MyPending;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener;

public class FragCerca extends Fragment implements OnAddGeofencesResultListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{

	private SharedPreferences spConfigs;
	private SharedPreferences.Editor spConfigsEditor;
	
	private boolean isRunning = false;
	
	public static FragCerca newInstance() {
		FragCerca fragment = new FragCerca();
		return fragment;
	}

	public FragCerca() {
	}
	
	private LocationClient mLocationClient;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewRoot = inflater.inflate(R.layout.fragment_cerca, null);
		
		mLocationClient = new LocationClient(getActivity(), this, this);
		mLocationClient.connect();
		
		spConfigs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		spConfigsEditor = spConfigs.edit();
		
		isRunning = spConfigs.getBoolean("geofenceRunning", false);
		
		Button btn = (Button) viewRoot.findViewById(R.id.btnActionGeofence);
		btn.setText(isRunning?"Parar":"Iniciar");
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (isRunning){
					List<String> ids = new ArrayList<>();
					ids.add("one");
					mLocationClient.removeGeofences(ids, null);
					isRunning = false;
				} else {
					Geofence g = new Geofence.Builder()
					.setRequestId("one")
					.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT)
					.setCircularRegion(-23.562383, -46.656350, 500)
					.setExpirationDuration(-1)
					.build();
					
					java.util.List<Geofence> geofences = new ArrayList<>();
					geofences.add(g);
					
					Intent intent = new Intent(getActivity(), MyPending.class);
					PendingIntent pi = PendingIntent.getService(getActivity(), 0, intent,  0);
					
					mLocationClient.addGeofences(geofences, pi, FragCerca.this);
					isRunning = true;
				}
				spConfigsEditor.putBoolean("geofenceRunning", isRunning);
			}
		});
		
		return viewRoot;
	}

	@Override
	public void onAddGeofencesResult(int arg0, String[] arg1) {}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
}
