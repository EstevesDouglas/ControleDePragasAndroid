package com.example.controlepragas.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.controlepragas.R;
import com.temboo.Library.Facebook.Publishing.Post;
import com.temboo.Library.Facebook.Publishing.Post.PostInputSet;
import com.temboo.Library.Facebook.Publishing.Post.PostResultSet;
import com.temboo.Library.Google.Gmail.SendEmail;
import com.temboo.Library.Google.Gmail.SendEmail.SendEmailInputSet;
import com.temboo.Library.Google.Gmail.SendEmail.SendEmailResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class ServiceCheck extends Service {

	private TimerTask tTask;
	private Timer timer;
	private boolean post = true;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (timer == null) {
			tTask = new TimerTask() {

				@Override
				public void run() {
					int i = (int) (Math.random() * 5);
					if (i == 2 && post) {
						post = false;
						NotificationCompat.Builder mBuilder = 
								new NotificationCompat.Builder(ServiceCheck.this)
								.setSmallIcon(R.drawable.ic_launcher)
								.setContentTitle("Notificação de Praga")
								.setContentText("Insper apresentou um alto nível de roedores!")
								/*.setContentIntent(pendingIntent)*/; // Required on
																	// Gingerbread
																	// and below

						/*final Intent emptyIntent = new Intent();
						PendingIntent pendingIntent = PendingIntent
								.getActivity(ctx, NOT_USED, emptyIntent,
										PendingIntent.FLAG_UPDATE_CURRENT);*/

						NotificationManager notificationManager = (NotificationManager)
								getSystemService(Context.NOTIFICATION_SERVICE);
						notificationManager.notify(1, mBuilder.build());
						
						sendEmail();
						sendFace();
						
					}
				}
			};
			timer = new Timer();
			timer.schedule(tTask, 1000, 1000);
		}
		return 1;
	}
	
	public void sendEmail(){
		new SendEmailTask().execute();
	}
	
	public void sendFace(){
		new SendFaceTask().execute();
	}
	
	class SendEmailTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
			try {
				TembooSession session = new TembooSession("ricardoogliari", "myFirstApp", "44172969-265b-46d3-8");
				
				SendEmail sendEmailChoreo = new SendEmail(session);

				// Get an InputSet object for the choreo
				SendEmailInputSet sendEmailInputs = sendEmailChoreo.newInputSet();

				// Set inputs
				sendEmailInputs.set_MessageBody("Boa noite senhor.\n\nO seu controle de pragas localizado na faculdade Insper constatou um número excessivo da presença de roedores.\n\nAtenciosamente.\nEquipe Controle de Pragas.");
				sendEmailInputs.set_Subject("Notificação do seu controle de pragas!");
				sendEmailInputs.set_Password("1Qaz2Wsx3Edc");
				sendEmailInputs.set_Username("rogliariping@gmail.com");
				sendEmailInputs.set_FromAddress("rogliariping@gmail.com");
				sendEmailInputs.set_ToAddress("rogliariping@gmail.com");

				// Execute Choreo
				SendEmailResultSet sendEmailResults = sendEmailChoreo.execute(sendEmailInputs);
			} catch (TembooException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}

	
	class SendFaceTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected Void doInBackground(Void... params) {
			// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
			try {
				// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
				TembooSession session = new TembooSession("ricardoogliari", "myFirstApp", "44172969-265b-46d3-8");

				Post postChoreo = new Post(session);

				// Get an InputSet object for the choreo
				PostInputSet postInputs = postChoreo.newInputSet();

				// Set inputs
				postInputs.set_AccessToken("CAAE6i7PZAJJkBAKRqKdeapA70BlHpGZApiFWA7C7QLCdZCe2nwjSjiZC4ZAiSKyd149l6AqTsRMob00pEhTLvOTy55viwL5HJ7pSjxuKWvGnTh8Ew0yuvRgd9N6dwRoGuYv5LqIrzUwNyxUsvWpYiYF01fMwQGlivE3UG9XqdCEoaOQKTz34o");
				postInputs.set_Message("Boa noite senhor.\n\nO seu controle de pragas localizado na faculdade Insper constatou um número excessivo da presença de roedores.\n\nAtenciosamente.\nEquipe Controle de Pragas.");

				// Execute Choreo
				PostResultSet postResults = postChoreo.execute(postInputs);
			} catch (TembooException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
