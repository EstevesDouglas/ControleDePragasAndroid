package com.example.controlepragas;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoe64.graphview.GraphView.GraphViewData;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragMapa extends Fragment {

	public static FragMapa newInstance() {
		FragMapa fragment = new FragMapa();
		return fragment;
	}

	public FragMapa() {
	}

	public static String AssetJSONFile(String filename, Context context)
			throws IOException {
		AssetManager manager = context.getAssets();
		InputStream file = manager.open(filename);
		byte[] formArray = new byte[file.available()];
		file.read(formArray);
		file.close();

		return new String(formArray);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewRoot = inflater.inflate(R.layout.fragment_mapa, null);

		final GoogleMap mMap = ((SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMyLocationEnabled(true);

		AQuery aq = new AQuery(getActivity());
		aq.ajax("http://10.92.101.35/MonitoramentoRoedores/consultatudo.php",
				JSONArray.class, new AjaxCallback<JSONArray>() {
					@Override
					public void callback(String url, JSONArray object,
							AjaxStatus status) {
						try {
							String rootStr = AssetJSONFile("graph_maps.json",
									getActivity());
							JSONObject root = new JSONObject(rootStr);
							JSONArray array = root.getJSONArray("kits");

							for (int i = 0; i < array.length(); i++) {
								JSONObject kit = array.getJSONObject(i);
								int qtd = i==0?object.length():kit.getInt("qtd");
								mMap.addMarker(new MarkerOptions()
										.position(
												new LatLng(
														kit.getDouble("latitude"),
														kit.getDouble("longitude")))
										.title(kit.getString("name"))
										.snippet(
												(i==0?object.length():kit.getInt("qtd")) + " roedores")
										.icon(BitmapDescriptorFactory
												.defaultMarker(qtd > 10 ? qtd > 25 ? BitmapDescriptorFactory.HUE_RED
														: BitmapDescriptorFactory.HUE_YELLOW
														: BitmapDescriptorFactory.HUE_GREEN)));

								mMap.addCircle(new CircleOptions()
										.center(new LatLng(kit
												.getDouble("latitude"), kit
												.getDouble("longitude")))
										.radius(1000)
										.strokeWidth(0)
										.fillColor(
												qtd > 10 ? qtd > 25 ? Color
														.argb(125, 255, 0, 0)
														: Color.argb(125, 255,
																255, 0) : Color
														.argb(125, 0, 255, 0)));
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				-23.598832, -46.676601), 15));

		return viewRoot;
	}

}
