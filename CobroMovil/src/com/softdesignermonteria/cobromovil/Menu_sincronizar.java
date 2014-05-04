package com.softdesignermonteria.cobromovil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Menu_sincronizar extends Activity {

	private Button bt_sincronizar_clientes;
	private Button bt_sincronizar_cobradores;
	private ListView lst;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_sincronizar);
		
		lst = (ListView) findViewById(R.id.lst);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {

			StrictMode.ThreadPolicy policy =

			new StrictMode.ThreadPolicy.Builder().permitAll().build();

			StrictMode.setThreadPolicy(policy);

			}
		
		Log.i(this.getClass().toString(), "Actividad Menu Clientes");
		bt_sincronizar_clientes = (Button) findViewById(R.id.bt_sincronizar_clientes);
		bt_sincronizar_clientes.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.i(this.getClass().toString(), "Presiona Boton Sincronizar");
				// setContentView(R.layout.activity_menu_clientes);
				if (sincronizar_clientes()) {
					System.out
							.println("Clientes Sincronizados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Clientes Sincronizados Satisfactoriamente");
				} else {
					System.out.println("Oops no sincronizados");
					Log.i(this.getClass().toString(), "Oops clientes no sincronizados");
				}
			}
		});
		
		Log.i(this.getClass().toString(), "Actividad Menu Cobradores");
		bt_sincronizar_cobradores = (Button) findViewById(R.id.bt_sincronizar_cobradores);
		bt_sincronizar_cobradores.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.i(this.getClass().toString(), "Presiona Boton Sincronizar");
				// setContentView(R.layout.activity_menu_clientes);
				if (sincronizar_cobradores()) {
					System.out
							.println("Cobradores Sincronizados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Cobradores Sincronizados Satisfactoriamente");
				} else {
					System.out.println("Oops no sincronizados");
					Log.i(this.getClass().toString(), "Oops cobradores no sincronizados");
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_sincronizar, menu);
		return true;
	}

	public boolean sincronizar_clientes() {
		boolean sw = true;

		// Abrimos la base de datos
		Log.i(this.getClass().toString(),
				"Entrando a Funcion Sincronizar Clientes");
		
		try {
			Log.i(this.getClass().toString(), "Antes de Abrir Database");
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,
					"cobro_movil", null, 4);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				Log.i(this.getClass().toString(),
						"Base de datos abierta en modo lectura y escritura");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(
						"http://inversionesjd.dydsoluciones.net/clientes_movil/extraer_clientes");
				del.setHeader("content-type", "application/json");
				Log.i(this.getClass().toString(),
						"Url : http://inversionesjd.dydsoluciones.net/clientes_movil/extraer_clientes");
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respJSON = new JSONArray(respStr);
				
				String[] clientes = new String[respJSON.length()];
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);
					int clientes_id = obj.getInt("id");
					String nombres = obj.getString("nombres");
					String direccion = obj
							.getString("direccion");
					String telefono = obj.getString("telefono");
					String celular = obj.getString("celular");
					String sql_insert_clientes = "insert into clientes "
							+ " (clientes_id,nombres,direccion,telefono,celular) "
							+ "values" + " (" + clientes_id + ",'" + nombres
							+ "','" + direccion + "','"
							+ telefono + "','" + celular + "') ";
					Log.i(this.getClass().toString(),sql_insert_clientes);
					db.execSQL(sql_insert_clientes);
					
					clientes[i] = "" + clientes_id + "-" + nombres + "-" + telefono + "-"+ celular ;
				}

				// Rellenamos la lista con los resultados
				ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, clientes);
				lst.setAdapter(adaptador);
				
			}
			
			//db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!", ex);
			sw = false;
		}

		return sw;
		

	}
	
	public boolean sincronizar_cobradores() {
		boolean sw = true;

		// Abrimos la base de datos
		Log.i(this.getClass().toString(),
				"Entrando a Funcion Sincronizar Cobradores");
		
		try {
			Log.i(this.getClass().toString(), "Antes de Abrir Database");
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,
					"cobro_movil", null, 4);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				Log.i(this.getClass().toString(),
						"Base de datos abierta en modo lectura y escritura");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(
						"http://inversionesjd.dydsoluciones.net/cobradores_movil/extraer_cobradores");
				del.setHeader("content-type", "application/json");
				Log.i(this.getClass().toString(),
						"Url : http://inversionesjd.dydsoluciones.net/cobradores_movil/extraer_cobradores");
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respJSON = new JSONArray(respStr);
				
				String[] cobradores = new String[respJSON.length()];
				
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);
					int cobradores_id = obj.getInt("id");
					String nombres = obj.getString("nombres");
					String direccion = obj
							.getString("direccion");
					String telefono = obj.getString("telefono");
					String celular = obj.getString("celular");
					String sql_insert_cobradores = "insert into cobradores "
							+ " (cobradores_id,nombres,direccion,telefono,celular) "
							+ "values" + " (" + cobradores_id + ",'" + nombres
							+ "','" + direccion + "','"
							+ telefono + "','" + celular + "') ";
					Log.i(this.getClass().toString(),sql_insert_cobradores);
					db.execSQL(sql_insert_cobradores);
					
					cobradores[i] = "" + cobradores_id + "-" + nombres + "-" + telefono + "-"+ celular ;
				}

				// Rellenamos la lista con los resultados
				ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, cobradores);
				lst.setAdapter(adaptador);
				
			}
			
			//db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!", ex);
			sw = false;
		}

		return sw;

	}

}
