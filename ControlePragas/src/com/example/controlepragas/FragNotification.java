package com.example.controlepragas;

import com.example.controlepragas.service.ServiceCheck;
import com.google.android.gms.internal.cb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class FragNotification extends Fragment{

	private Button btnAction;
	
	private SharedPreferences spConfigs;
	private SharedPreferences.Editor spConfigsEditor;
	
	private CheckBox ckEmail;
	private CheckBox ckFacebook;
	private CheckBox ckTwitter;
	private CheckBox ckSmartphone;
	
	private EditText edtQtd;
	
	private boolean serviceIsRunning = false;
	
	public static FragNotification newInstance() {
		FragNotification fragment = new FragNotification();		
		return fragment;
	}

	public FragNotification() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewRoot = inflater.inflate(R.layout.fragment_notification, null);
		
		spConfigs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		spConfigsEditor = spConfigs.edit();
		
		ckEmail = (CheckBox) viewRoot.findViewById(R.id.cbEmail);
		ckFacebook = (CheckBox) viewRoot.findViewById(R.id.cbFacebook);
		ckSmartphone = (CheckBox) viewRoot.findViewById(R.id.cbSmartphone);
		ckTwitter = (CheckBox) viewRoot.findViewById(R.id.cbTwitter);
		
		edtQtd = (EditText) viewRoot.findViewById(R.id.edtQtd);
		
		btnAction = (Button) viewRoot.findViewById(R.id.btnAction);
		btnAction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (serviceIsRunning){
					spConfigsEditor.putBoolean("facebook", ckFacebook.isChecked());
					spConfigsEditor.putBoolean("email", ckEmail.isChecked());
					spConfigsEditor.putBoolean("smartphone", ckSmartphone.isChecked());
					spConfigsEditor.putBoolean("twitter", ckTwitter.isChecked());
					spConfigsEditor.putString("qtd", edtQtd.getText().toString());
				} else {
					Intent intent = new Intent(getActivity(), ServiceCheck.class);
					getActivity().startService(intent);
					serviceIsRunning = true;
				}
				
				spConfigsEditor.commit();
			}
		});
		
		ckEmail.setChecked(spConfigs.getBoolean("email", false));
		ckFacebook.setChecked(spConfigs.getBoolean("facebook", false));
		ckSmartphone.setChecked(spConfigs.getBoolean("smartphone", false));
		ckTwitter.setChecked(spConfigs.getBoolean("twitter", false));
		edtQtd.setText(spConfigs.getString("qtd", ""));
		
		serviceIsRunning = spConfigs.getBoolean("running", false);
		btnAction.setText(serviceIsRunning?"Parar":"Iniciar");
				
		return viewRoot;
	}
	
}
