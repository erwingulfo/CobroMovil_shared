package com.softdesignermonteria.cobromovil;

import org.apache.http.auth.BasicUserPrincipal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuPrincipal extends Activity {
	
	
	private TextView userlogueado;
	private TextView nombre_usuario;
	private TextView clave_usuario;
	private Button b_m_sincronizar;
	private Button b_m_clientes;
	private Button b_m_recaudos;
	private Button b_m_reportes;
	
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
		
		b_m_sincronizar = (Button)findViewById(R.id.m_reportes);
		b_m_sincronizar.setOnClickListener(new View.OnClickListener() {
		
		//Intent in = new Intent("com.softdesignermonteria.cobromovil.prueba");
		public void onClick(View arg0){
			
			//startActivity(in);
			  Intent i = new Intent();
			  i.setClass(MenuPrincipal.this, Menu_sincronizar.class);
			  startActivity(i);
			}
		});
		
		b_m_clientes = (Button)findViewById(R.id.b_m_clientes);
		b_m_clientes.setOnClickListener(new View.OnClickListener() {
		
		//Intent in = new Intent("com.softdesignermonteria.cobromovil.prueba");
		public void onClick(View arg0){
			
			//startActivity(in);
			  Intent i = new Intent();
			  i.setClass(MenuPrincipal.this, Busqueda_clientes.class);
			  startActivity(i);
			}
		});
		
		b_m_recaudos = (Button)findViewById(R.id.b_m_recaudos);
		b_m_recaudos.setOnClickListener(new View.OnClickListener() {
		
		//Intent in = new Intent("com.softdesignermonteria.cobromovil.prueba");
		public void onClick(View arg0){
			
			//startActivity(in);
			  Intent i = new Intent();
			  i.setClass(MenuPrincipal.this, Recaudos.class);
			  startActivity(i);
			}
		});
		
		/*probando camps  alejandro betancourt*/
		
	
		b_m_reportes = (Button)findViewById(R.id.b_m_reportes);
		b_m_reportes.setOnClickListener(new View.OnClickListener() {
		
		//Intent in = new Intent("com.softdesignermonteria.cobromovil.prueba");
		public void onClick(View arg0){
			
			//startActivity(in);
			  Intent i = new Intent();
			  i.setClass(MenuPrincipal.this, Reportes.class);
			  startActivity(i);
			}
		});
		
		/*probando camps  alejandro betancourt*/
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.menu_sincronizar, menu);
			return true;
		}

}
