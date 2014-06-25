package com.softdesignermonteria.cobromovil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Clientes extends Activity {

	private Button enviar;
	private EditText cedula;
	private EditText nombre1;
	private EditText nombre2;
	private EditText apellido1;
	private EditText apellido2;

	private EditText direccion;
	private EditText telefono;
	private EditText valor;

	private String Msg = "";

	private String url_servidor;
	private String nombre_database;
	private int version_database;

	// private String Msg="";

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientes);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		/**
		 * Asignacion de valores de variables globales android se especifica
		 * nombre de la base de datos version de la base de datos url de los
		 * webservices
		 */

		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

		url_servidor = globalVariable.getUrl_servidor();
		nombre_database = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();

		cedula = (EditText) findViewById(R.id.TextClientesCedula);
		nombre1 = (EditText) findViewById(R.id.TextClientesNombre1);
		nombre2 = (EditText) findViewById(R.id.TextClkientesNombre2);
		apellido1 = (EditText) findViewById(R.id.TxtClientesApellido1);
		apellido2 = (EditText) findViewById(R.id.TextClientesApellido2);
		direccion = (EditText) findViewById(R.id.TextClientesDireccion);
		telefono = (EditText) findViewById(R.id.TextClientesTelefono);

		valor = (EditText) findViewById(R.id.TextClientesValorAPrestar);

		enviar = (Button) findViewById(R.id.ButtonClientesEnviar);

		enviar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(url_servidor
						+ "/clientes_movil/add/");
				post.setHeader("content-type", "application/json");

				// Construimos el objeto cliente en formato JSON
				JSONObject dato = new JSONObject();

				try {

					dato.put("cedula", cedula.getText().toString());
					dato.put("nombre1", nombre1.getText().toString());
					dato.put("nombre2", nombre2.getText().toString());
					dato.put("apellido1", apellido1.getText().toString());
					dato.put("apellido2", apellido2.getText().toString());
					dato.put("direccion", direccion.getText().toString());
					dato.put("telefono", telefono.getText().toString());
					dato.put("valor", valor.getText().toString());

					StringEntity entity;
					try {
						entity = new StringEntity(dato.toString());

						post.setEntity(entity);

						HttpResponse resp;
						try {
							resp = httpClient.execute(post);
							Log.i(this.getClass().toString(),"Se envio al Cliente a guadar");

							System.out.println(post.getURI());
							System.out.println(resp.getParams());
							String respStr = EntityUtils.toString(resp.getEntity());
							System.out.println(respStr.getBytes());
							JSONArray respJSON = new JSONArray(respStr);
							JSONObject obj = respJSON.getJSONObject(0); 
							 
							if(obj.getBoolean("mensaje")==true){
								//db.execSQL("UPDATE recaudos SET sincronizado='1' WHERE provisional= '"+provisional+"' ");
								
								Log.i(this.getClass().toString(),"Success: "+ obj.getBoolean("descripcion") );
							}else{
								Log.i(this.getClass().toString(),"Error: "+ obj.getBoolean("descripcion") );
								//Toast.makeText(Menu_sincronizar.this, "Error Sincronizando Recibo provisional= '"+provisional+"' ", Toast.LENGTH_SHORT).show();
								
								//errores="Usp Error cargado Recibos al servidor";
							}

						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.i(this.getClass().toString(),"Error: "+ e.getMessage() );
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Log.i(this.getClass().toString(),"Error: "+ e.getMessage() );
						}

					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.i(this.getClass().toString(),"Error: "+ e.getMessage() );
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.i(this.getClass().toString(),"Error: "+ e.getMessage() );
				}

			}
		});

	}

	public boolean AgregarClientes() {

		return true;
	}

}
