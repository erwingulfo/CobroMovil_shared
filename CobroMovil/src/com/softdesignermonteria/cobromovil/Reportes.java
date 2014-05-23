package com.softdesignermonteria.cobromovil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;


public class Reportes extends Activity {
	
	private TextView tv1;
	private TextView tv7;
	
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	private String user_logueado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reportes);
		
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		user_logueado = globalVariable.getUserlogueado();
		
		tv1 = (TextView)findViewById(R.id.tv1);
		tv7 = (TextView)findViewById(R.id.tv7);
		tv7.setText(user_logueado);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reportes, menu);
		return true;
	}

}
