package com.softdesignermonteria.cobromovil;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Menu_sincronizar extends Activity {

	private Button bt_sincronizar_clientes;
	private Button bt_sincronizar_cobradores;
	private Button bt_sincronizar_cartera;
	private Button probando;
	private ProgressBar bprogreso;
	
	private MiTareaAsincronaDialog tarea2;
	
	private ProgressDialog pDialog;
	
	//private ListView lst;
	
	/*
	 * Pegar este codigo en todas las actividades que lo requieran
	 * */
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_sincronizar);
		
		probando = (Button)findViewById(R.id.probando);
	    		
		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 * */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		
		//lst = (ListView) findViewById(R.id.lst);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy =
			new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

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
		
		//bt_sincronizar_cartera
		
		bt_sincronizar_cartera = (Button) findViewById(R.id.bt_sincronizar_cartera);
		bt_sincronizar_cartera.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Log.i(this.getClass().toString(), "Presiona Boton Sincronizar");
				// setContentView(R.layout.activity_menu_clientes);
				if (sincronizar_cartera()) {
					System.out
							.println("Cartera Sincronizados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Cobradores Sincronizados Satisfactoriamente");
				} else {
					System.out.println("Oops no sincronizados");
					Log.i(this.getClass().toString(), "Oops Cartera no sincronizados");
				}
			}
		});
		
		probando.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				pDialog = new ProgressDialog(Menu_sincronizar.this);
				pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pDialog.setMessage("Sincronizando...");
				pDialog.setCancelable(true);
				pDialog.setMax(100);
				
				tarea2 = new MiTareaAsincronaDialog();
				tarea2.execute();
				
				
			}
		});
			
		
	}
	
	private void tareaLarga()
    {
    	try { 
    		Thread.sleep(1000); 
    	} catch(InterruptedException e) {}
    }
	
		
    private class MiTareaAsincronaDialog extends AsyncTask<Void, Integer, Boolean> {
    	
    	@Override
    	protected Boolean doInBackground(Void... params) {
    		
    		for(int i=1; i<=5; i++) {
				tareaLarga();
				publishProgress(i*5);
				
				if(isCancelled())
					break;
			}
    		
    		return true;
    	}
    	
    	@Override
    	public void onProgressUpdate(Integer... values) {
    		int progreso = values[0].intValue();
    		pDialog.setProgress(progreso);
    	}
    	
    	@Override
    	protected void onPreExecute() {
    		
    		pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					MiTareaAsincronaDialog.this.cancel(true);
				}
			});
    		
    		pDialog.setProgress(0);
    		pDialog.show();
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result) {
    		if(result)
    		{
    			pDialog.dismiss();
    			Toast.makeText(Menu_sincronizar.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
    		}
    	}
    	
    	@Override
    	protected void onCancelled() {
    		Toast.makeText(Menu_sincronizar.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
    	}
    }

	

	public boolean sincronizar_clientes() {
		boolean sw = true;
		
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"clientes_movil/extraer_clientes");
				del.setHeader("content-type", "application/json");
				
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
					
					//clientes[i] = "" + clientes_id + "-" + nombres + "-" + telefono + "-"+ celular ;
					
				}
				
				// Rellenamos la lista con los resultados
				/*ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, clientes);
				lst.setAdapter(adaptador);*/
				mostrar_sincronizados(respJSON.length(),"Clientes sincronizados");
				
								
			}
			
			db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!", ex);
			sw = false;
		}

		return sw;
		

	}
	
	public boolean sincronizar_cobradores() {
		boolean sw = true;

		
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"cobradores_movil/extraer_cobradores");
				del.setHeader("content-type", "application/json");
				
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
				/*ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, cobradores);
				lst.setAdapter(adaptador);*/
				mostrar_sincronizados(respJSON.length(),"Cobradores sincronizados");
				
			}
			
			db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!", ex);
			sw = false;
		}

		return sw;

	}
	
	public boolean sincronizar_cartera() {
		boolean sw = true;
		
		try {
			
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"cartera_movil/extraer_cartera");
				del.setHeader("content-type", "application/json");
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respJSON = new JSONArray(respStr);
				
				String[] cartera = new String[respJSON.length()];
				
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);
					
					int detalle_cxc_id = obj.getInt("detalle_cxc_id");
					int cobradores_id  = obj.getInt("cobradores_id");
					int creditos_id    = obj.getInt("creditos_id");
					int clientes_id    = obj.getInt("clientes_id");
					double valor       = obj.getDouble("valor");
					
					String vencimiento     = obj.getString("vencimiento");
					String cedula          = obj.getString("cedula");
					String cedula_cobrador = obj.getString("cedula_cobrador");
					
					
					String sql_insert_caretra = "insert into cartera "
							+ " (detalle_cxc_id,creditos_id,clientes_id,cedula,cobradores_id,cedula_cobrador,vencimiento,valor) "
							+ "values ( '"+ detalle_cxc_id +"','"+ creditos_id +"','"+ clientes_id +"','"+ cedula +"','"+ cobradores_id +"','"+ cedula_cobrador +"','"+ vencimiento +"','"+ valor +"') "; 
								
					Log.i(this.getClass().toString(),sql_insert_caretra);
					db.execSQL(sql_insert_caretra);
					
					cartera[i] = "" + detalle_cxc_id + "-" + creditos_id + "-" + clientes_id + "-"+ cedula ;
				}

				// Rellenamos la lista con los resultados
				/*ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, cartera);
				lst.setAdapter(adaptador);*/
				mostrar_sincronizados(respJSON.length(),"Cartera sincronizada");
			
				
				
			}
			
			db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!", ex);
			sw = false;
		}

		return sw;

	}
	
	/*funcion para mostrar en forma de alerta los clientes, cobradores y la cartera sincronizada.*/
	
	public void mostrar_sincronizados(int num_items,String mensaje){
		
		Toast alerta= Toast.makeText(this, mensaje+" : "+num_items, Toast.LENGTH_SHORT);
		alerta.show();
		
	}
	
		
	
}
