package com.softdesignermonteria.cobromovil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

		private Button entrar;
		private Button registrarse;
		private EditText usuario;
		private EditText clave;
		private SQLiteDatabase db;
		private TextView userlogueado;

		private String url_servidor;
		private String nombre_database;
		private int version_database;
				
        public void onCreate(Bundle savedInstanceState){
        	
        	super.onCreate(savedInstanceState);
        	
			setContentView(R.layout.activity_main);
			/**
			 * 
			 * Lanzando servicio por debajo de sincronizacion de recaudos
			 * 
			 * */
			Intent service = new Intent(this, SincronizarCobroMovil.class);
			startService(service);
			
			/**
			 * Asignacion de valores de variables globales android
			 * se especifica nombre de la base de datos
			 * version de la base de datos
			 * url de los webservices
			 */
				final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
				
				/*
				 * 
				 * Definicion y asignacion de valores globales para la app
				 * 
				 * */
				globalVariable.setNombre_database("cobro_movil");
				globalVariable.setVersion_database(11);
				globalVariable.setUrl_servidor("http://inversionesjd.dydsoluciones.net/");
				
				/*
				 * 
				 * asignacion de valores a variables privadas de esta clase
				 * 
				 * */
				url_servidor     = globalVariable.getUrl_servidor();
				nombre_database  = globalVariable.getNombre_database();
				version_database = globalVariable.getVersion_database();
								
			/*
			 * 
			 * Obtenemos las referencias a los controles
			 */
			usuario = (EditText)findViewById(R.id.usuario);
			clave = (EditText)findViewById(R.id.clave);
			userlogueado = (TextView)findViewById(R.id.userlogueado);
			entrar = (Button)findViewById(R.id.entrar);
			registrarse = (Button)findViewById(R.id.BtnRegistrarse);
			usuario.requestFocus();			
			/*
			 * 
			 * Abrimos la base de datos 'Cobro' en modo escritura
			 */
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this, nombre_database, null, version_database);
	        db = usdbh.getWritableDatabase();
			
			/*
			 * 
			 * Asociamos al evento Onclik la validación del usuario y la clave contra la bd
			 */
	        entrar.setOnClickListener(new OnClickListener() {
				
	        		public void onClick(View v) {
					
							//Recuperamos los valores de los campos de texto
							String usu = usuario.getText().toString();
							String cla = clave.getText().toString();
		
							//Alternativa 1: método rawQuery()
							Cursor c = db.rawQuery("select nombre,clave,cobradores_id,cedula_cobrador from usuarios where nombre='" + usu + "' and clave='" + md5(cla) + "'", null);
							
							Log.e("SOY MD5:","select nombre,clave,cobradores_id,cedula_cobrador from usuarios where nombre='" + usu + "' and clave='" + md5(cla) + "'");
												
							if (c.moveToFirst()) {
								//String actionName= "com.softdesignermonteria.cobromovil.MenuPrincipal";
								Intent i = new Intent();
								i.setClass(MainActivity.this, MenuPrincipal.class);
						        i.putExtra("pnombre_usuario", usuario.getText().toString());
						        i.putExtra("pclave_usuario", clave.getText().toString());
						      
						        int index = c.getColumnIndex("cobradores_id");
						        String cobradores_id = c.getString(index);
						        
						        Log.e("Main Activity","cobradores_id = " + cobradores_id);
						        
						        int index2 = c.getColumnIndex("cedula_cobrador");
						        String cedula_cobrador = c.getString(index2);
						        
						        int index3 = c.getColumnIndex("nombre");
						        String nombre_usuario = c.getString(index3);
						        
						        globalVariable.setCobradores_id(cobradores_id);
						        globalVariable.setCedula_cobrador(cedula_cobrador);
						        globalVariable.setUserlogueado(nombre_usuario);
						        
						        
						        startActivity(i);
								
						    }else{
								
						    	query_vacio(v);
						    	usuario.setText("");
					            clave.setText("");
					            usuario.requestFocus();
								
							 }
	        		}
				
			});
	        
	        
	        
	        registrarse.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					//String actionName= "com.softdesignermonteria.cobromovil.MenuPrincipal";
					Intent i = new Intent();
					i.setClass(MainActivity.this, MenuPrincipal.class);
			        i.putExtra("pnombre_usuario", usuario.getText().toString());
			        i.putExtra("pclave_usuario", clave.getText().toString());
			        
			        startActivity(i);
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
        
  
        public static String md5(String s){
            MessageDigest digest;
            try 
            {
                digest = MessageDigest.getInstance("MD5");
                digest.update(s.getBytes(),0,s.length());
                String hash = new BigInteger(1, digest.digest()).toString(16);
                return hash;
            } 
            catch (NoSuchAlgorithmException e) 
            {
                e.printStackTrace();
            }
            return "";
        }

  }
