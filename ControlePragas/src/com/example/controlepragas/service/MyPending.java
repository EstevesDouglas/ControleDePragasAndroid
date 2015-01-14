package com.example.controlepragas.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.controlepragas.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

public class MyPending extends IntentService {
    public MyPending(String name) {
		super(name);
	}
    
    public MyPending() {
		super("MyPending");
	}

	protected void onHandleIntent(Intent intent) {
            int transitionType = LocationClient.getGeofenceTransition(intent);
            
            NotificationCompat.Builder mBuilder = 
					new NotificationCompat.Builder(MyPending.this)
					.setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Notificação de Praga")
					.setContentText(
							transitionType == Geofence.GEOFENCE_TRANSITION_ENTER?
							"Cuidado, você está a menos de 500m do Insper, um foco de roedores!":
								"Ufa, você está a mais de 500m do Insper, um foco de roedores!");

			NotificationManager notificationManager = (NotificationManager)
					getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(10, mBuilder.build());
    }
}