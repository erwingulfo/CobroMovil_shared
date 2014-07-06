package com.softdesignermonteria.cobromovil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
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
		private Context context;
		
		
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override		
        public void onCreate(Bundle savedInstanceState){
        	
        	super.onCreate(savedInstanceState);
        	
			setContentView(R.layout.activity_main);
			
			context = this;
			/**
			 * 
			 * Lanzando servicio por debajo de sincronizacion de recaudos
			 * 
			 * */
			
				Intent service = new Intent(this, SincronizarCobroMovil.class);
				startService(service);
				
				
				if (android.os.Build.VERSION.SDK_INT > 9) {
					StrictMode.ThreadPolicy policy =
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
				}
			
			
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
					
					TablasSQLiteHelper usdbh = new TablasSQLiteHelper(context,nombre_database, null, version_database);
					SQLiteDatabase db = usdbh.getWritableDatabase();
					
					// Si hemos abierto correctamente la base de datos
				
						
					try {
						
						JSONObject admin = new JSONObject();
						admin.put("usuario", usuario.getText().toString());
						admin.put("clave",   clave.getText().toString());
						
						try {
							
							String jsonencabezado    = URLEncoder.encode(admin.toString(), "UTF-8");
							
							HttpClient httpClient = new DefaultHttpClient();
							HttpPost post = new HttpPost(url_servidor+"login_movil/validar/?login="+jsonencabezado);
							post.setHeader("content-type", "application/json");
							System.out.println(post.getURI());
							
							System.out.println("ANTES DEL POST de ejecutar");
							
							HttpResponse resp = httpClient.execute(post);
							
							System.out.println("despues del DEL POST de ejecutar");
							
							String respStr = EntityUtils.toString(resp.getEntity());
							JSONArray respJSON = new JSONArray(respStr);
							JSONObject obj = respJSON.getJSONObject(0);
						
							System.out.println("despues de ejecutar");
							
							//System.out.println(resp.getParams());
							//System.out.println(respStr.getBytes());
							//System.out.println(admin);
							
							if(obj.getBoolean("mensaje")==true){
								if (db != null) {
									
									System.out.println("si mensaje es true");
									
									
									
										
									
									JSONObject obj2 = respJSON.getJSONObject(1);
									
									db.execSQL("delete  from usuarios where cobradores_id = '"+obj2.getString("cobradores_id")+"' ");
									System.out.println("delete  from usuarios where cobradores_id = '"+obj2.getString("cobradores_id")+"' ");
									
									
									String insert_usu = " insert into usuarios "
															+ "(nombre,clave,cobradores_id,cedula_cobrador) "
															+ " values "
															+ "("
															+ " '" +obj2.getString("nombre") + "', "
															+ " '" +obj2.getString("clave") + "', "
															+ " '" +obj2.getString("cobradores_id") + "', "
															+ " '" +obj2.getString("cedula_cobrador") + "'  "
															+ ");";
									
									System.out.println(insert_usu);
									
									db.execSQL(insert_usu);
									
									System.out.println("si mensaje es false");
									
									usuario_validado(v);
									
									System.out.println("si mensaje es false 2");
								}else{
									 error_validacion(v,"No se Pudo Abrir la base de datos");
								}
								db.close();
							}else{
							
								error_validacion(v,obj.getString("descripcion").toString());
							}
							
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println(e.getMessage());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							System.out.println(e.getMessage());
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println(e.getMessage());
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
			Toast toast1 = Toast.makeText(this, " Usuario y/o clave errados ", Toast.LENGTH_SHORT);
	        toast1.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	        toast1.show();
        }
        
        public void usuario_validado(View v) {
			Toast toast2 = Toast.makeText(this, "Usuario Validado Satisfactoriamente / Puede loguearse Normanlmente", Toast.LENGTH_SHORT);
	        toast2.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	        toast2.show();
        }
        
        public void usuario_no_validado(View v) {
			Toast toast3 = Toast.makeText(this, " Usuario No Existe y/o Usuario y Contraseña Incorrectos ", Toast.LENGTH_SHORT);
	        toast3.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	        toast3.show();
        }
        
        public void error_validacion(View v, String msg) {
			Toast toast4 = Toast.makeText(this, msg , Toast.LENGTH_SHORT);
	        toast4.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	        toast4.show();
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
