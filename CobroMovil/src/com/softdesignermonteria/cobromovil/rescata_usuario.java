package com.softdesignermonteria.cobromovil;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

public class rescata_usuario extends Activity {
	
	//Variable utilizadas en la clase
	 private TextView userlogueado;
	

	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_menu_principal);

	  //Referencia a los objetos del layout
	  userlogueado = (TextView) findViewById( R.id.userlogueado );
	  //Recupera parametros y los muestra en el TextView
	  Intent intent = getIntent();
	  Bundle bundle = intent.getExtras();
	  
	  if ( bundle != null ) {
	  
		  userlogueado.setText(bundle.getString("nombre_usuario"));
	  
	  }
	 
	 }
	  

}
