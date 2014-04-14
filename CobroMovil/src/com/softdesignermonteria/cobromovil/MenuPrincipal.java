package com.softdesignermonteria.cobromovil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuPrincipal extends Activity {
	
	
	private TextView userlogueado;
	private TextView nombre_usuario;
	private TextView clave_usuario;
	private Button b_m_clientes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Comentario agregado por el clientes
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_principal);
	
		userlogueado = (TextView) findViewById(R.id.userlogueado);
		nombre_usuario = (TextView) findViewById(R.id.nombre_usuario);
		//clave_usuario = (TextView) findViewById(R.id.clave_usuario);
		Bundle bundle = getIntent().getExtras();
		userlogueado.setText(bundle.getString("pnombre_usuario"));
		//nombre_usuario.setText(bundle.getString("pnombre_usuario"));
		//clave_usuario.setText(bundle.getString("pclave_usuario"));
		b_m_clientes = (Button)findViewById(R.id.b_m_clientes);
		b_m_clientes.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View arg0){
		    	setContentView(R.layout.activity_menu_clientes); 
			}
		});
		
		/*probando cambios  alejandro betancourt*/
	}
	
	 
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_principal, menu);
		return true;
	}

}
