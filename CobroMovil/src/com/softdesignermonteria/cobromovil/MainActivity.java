package com.softdesignermonteria.cobromovil;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

		private Button entrar;
		private EditText usuario;
		private EditText clave;
		private SQLiteDatabase db;
		private TextView userlogueado;
		
				
        public void onCreate(Bundle savedInstanceState){
        	
        	super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			
			
			//LblMensaje = (TextView)findViewById(R.id.LblMensaje);
								
			//Obtenemos las referencias a los controles
			usuario= (EditText)findViewById(R.id.usuario);
			clave = (EditText)findViewById(R.id.clave);
			userlogueado = (TextView)findViewById(R.id.userlogueado);
			entrar = (Button)findViewById(R.id.entrar);
						
			//Abrimos la base de datos 'Cobro' en modo escritura
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this, "cobro_movil", null, 3);
	        db = usdbh.getWritableDatabase();
			
			//Asociamos al evento Onclik la validación del usuario y la clave contra la bd
	        entrar.setOnClickListener(new OnClickListener() {
				
			public void onClick(View v) {
					
					//Recuperamos los valores de los campos de texto
					String usu = usuario.getText().toString();
					String cla = clave.getText().toString();

					//Alternativa 1: método rawQuery()
					Cursor c = db.rawQuery("select nombre,clave from usuarios where nombre='" + usu + "' and clave='" + cla + "'", null);
					
					if (c.moveToFirst()) {
						//String actionName= "com.softdesignermonteria.cobromovil.MenuPrincipal";
						Intent i = new Intent();
						i.setClass(MainActivity.this, MenuPrincipal.class);
				        i.putExtra("pnombre_usuario", usuario.getText().toString());
				        i.putExtra("pclave_usuario", clave.getText().toString());
				        startActivity(i);
						
				    }else{
						
						
				    	query_vacio(v);
				    	usuario.setText("");
			            clave.setText("");
			            usuario.requestFocus();
				    	
				    	//Notificación(toast) personalizada con imagen
				        /*Toast toast3 = new Toast(getApplicationContext());
				        
				        LayoutInflater inflater = getLayoutInflater();
				        View layout = inflater.inflate(R.layout.mensaje_error,
				                        (ViewGroup) findViewById(R.id.lytLayout));
				 
				        TextView txtMsg = (TextView)layout.findViewById(R.id.txtMensaje);
				        txtMsg.setText("Error: Usuario y/o clave errados");
				 
				        toast3.setDuration(Toast.LENGTH_SHORT);
				        toast3.setView(layout);
				        toast3.show();*/
				        
						
					 }
				}
				
			});
	        
	        
						
		}
        
        public void campos_vacios(View v) {
    		Toast toast = Toast.makeText(this, "Campo usuario y/o clave vacio", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }
    
        public void query_vacio(View v) {
			Toast toast = Toast.makeText(this, "Usuario y/o clave errados", Toast.LENGTH_SHORT);
	        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	        toast.show();
        }

  }
