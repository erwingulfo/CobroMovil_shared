package com.softdesignermonteria.cobromovil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MenuPrincipal extends Activity {
	
	
	private TextView userlogueado;
	private TextView nombre_usuario;
	private TextView clave_usuario;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
	
		userlogueado = (TextView) findViewById(R.id.userlogueado);
		nombre_usuario = (TextView) findViewById(R.id.nombre_usuario);
		clave_usuario = (TextView) findViewById(R.id.clave_usuario);
		Bundle bundle = getIntent().getExtras();
		userlogueado.setText(bundle.getString("pnombre_usuario"));
		nombre_usuario.setText(bundle.getString("pnombre_usuario"));
		clave_usuario.setText(bundle.getString("pclave_usuario"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}

}
