package com.example.controlepragas;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	
	private FragGraph fragGraph;
	private FragMapa fragMapa;
	private FragCerca fragCerca;
	private FragNotification fragNotification;
	
	private FragmentTransaction fragTrans;
	
	private boolean isInitial = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
		
		fragMapa = (FragMapa)getSupportFragmentManager().findFragmentById(R.id.fragMapa);
		fragCerca = (FragCerca)getSupportFragmentManager().findFragmentById(R.id.fragCerca);
		fragGraph = (FragGraph)getSupportFragmentManager().findFragmentById(R.id.fragGraph);
		fragNotification = (FragNotification)getSupportFragmentManager().findFragmentById(R.id.fragNotification);
		
		fragTrans = getSupportFragmentManager().beginTransaction();
		fragTrans.hide(fragGraph);
		fragTrans.hide(fragNotification);
		fragTrans.hide(fragMapa);
		fragTrans.hide(fragCerca);
		fragTrans.commit();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (isInitial){
			isInitial = false;
			return;
		}
		
		FragmentManager fragmentManager = getSupportFragmentManager();

		switch (position) {
		case 0:
			fragTrans = getSupportFragmentManager().beginTransaction();
			fragTrans.hide(fragGraph);
			fragTrans.hide(fragCerca);
			fragTrans.hide(fragNotification);
			fragTrans.show(fragMapa);
			fragTrans.commit();
			restoreActionBar("Mapa");
			break;
		case 1:
			fragTrans = getSupportFragmentManager().beginTransaction();
			fragTrans.show(fragGraph);
			fragTrans.hide(fragNotification);
			fragTrans.hide(fragMapa);
			fragTrans.hide(fragCerca);
			fragTrans.commit();
			restoreActionBar("Gráfico");
			break;
		case 2:
			fragTrans = getSupportFragmentManager().beginTransaction();
			fragTrans.hide(fragGraph);
			fragTrans.show(fragNotification);
			fragTrans.hide(fragMapa);
			fragTrans.hide(fragCerca);
			fragTrans.commit();
			restoreActionBar("Notificações");
			break;
		case 3:
			fragTrans = getSupportFragmentManager().beginTransaction();
			fragTrans.hide(fragGraph);
			fragTrans.hide(fragNotification);
			fragTrans.hide(fragMapa);
			fragTrans.show(fragCerca);
			fragTrans.commit();
			restoreActionBar("Cerca");
			break;
		}
		// update the main content by replacing fragments

	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		}
	}

	public void restoreActionBar(String title) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(title);
	}
}
