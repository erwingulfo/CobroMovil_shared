package com.softdesignermonteria.cobromovil;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SincronizarCobroMovil extends Service {
	private Timer mTimer = null;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mTimer = new Timer();
		this.mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				ejecutarTarea();
			}
		}, 0, 1000 * 900);
	}

	private void ejecutarTarea() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				NotifyManager notify = new NotifyManager();
				notify.playNotification(getApplicationContext(),
						MenuPrincipal.class, "Tienes una notificación cada Quince Minutos",
						"Notificación", R.drawable.ic_action_cloud);

			}
		});
		t.start();
	}

}