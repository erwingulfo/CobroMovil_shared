package com.softdesignermonteria.cobromovil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Reportes extends Activity {
	
	private TextView tv1;
	private TextView tv7;
	private Button m_reportes;
	
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
		
		m_reportes= (Button)findViewById(R.id.m_reportes);
		
		m_reportes.setOnClickListener(new View.OnClickListener() {
		
		//Intent in = new Intent("com.softdesignermonteria.cobromovil.prueba");
		public void onClick(View arg0){
			
			//startActivity(in);
			  Intent i = new Intent();
			  i.setClass(Reportes.this, Cobro_Diario.class);
			  startActivity(i);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reportes, menu);
		return true;
	}

}
