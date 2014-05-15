package com.softdesignermonteria.cobromovil;


import java.util.ArrayList;

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
import android.preference.PreferenceScreen;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
	private Button bt_sincronizar_usuarios;
	private Button bt_sincronizar_todos;
	
	//private ListView lv;
	private ProgressBar bprogreso;
	
	private MiTareaAsincronaDialog tarea2;
	
	private ProgressDialog pDialog;
	
	//private ListView lst;
	
	/*
	 * Pegar este codigo en todas las actividades que lo requieran
	 * */
	private String url_servidor;
	private String nombre_database;
	
	private String proceso_clientes = "async_clientes"; 
	private String proceso_cobradores = "async_cobradores";
	private String proceso_cartera = "async_cartera";
	private String proceso_usuarios = "async_usuarios";
	
	private ProgressDialog pDialog_clientes;
	private ProgressDialog pDialog_cobradores;
	private ProgressDialog pDialog_cartera;
	private ProgressDialog pDialog_usuarios;
	
	private int version_database;
	public long id;
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_sincronizar);
		
		
		//lv = (ListView)findViewById(R.id.lv);
	
		ArrayList<String> menu = new ArrayList<String>();
		/*menu.add("Sincronizar Clientes");
		menu.add("Sincronizar Cobradores");
		menu.add("Sincronizar Cartera");
		menu.add("Sincronizar Todos");*/
		
		
		/*Dialogo Clientes*/
		
		pDialog_clientes = new ProgressDialog(Menu_sincronizar.this);
		pDialog_clientes.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog_clientes.setMessage("Sincronizando...Clientes");
		pDialog_clientes.setCancelable(true);
		//pDialog_clientes.setMax(100);
		
		
		/*Dialogo Cobradores*/
		
		pDialog_cobradores = new ProgressDialog(Menu_sincronizar.this);
		pDialog_cobradores.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog_cobradores.setMessage("Sincronizando...Cobradores");
		pDialog_cobradores.setCancelable(true);
		//pDialog_cobradores.setMax(100);
		
		/*Dialogo Cartera*/
		
		pDialog_cartera = new ProgressDialog(Menu_sincronizar.this);
		pDialog_cartera.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog_cartera.setMessage("Sincronizando...Cartera");
		pDialog_cartera.setCancelable(true);
		//pDialog_cartera.setMax(100);
		
		/*Dialogo Usuarios del Sistema*/
		
		pDialog_usuarios = new ProgressDialog(Menu_sincronizar.this);
		pDialog_usuarios.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog_usuarios.setMessage("Sincronizando...Usuarios");
		pDialog_usuarios.setCancelable(true);
		
		
		
		/*ArrayAdapter<String> adaptador =
		new ArrayAdapter<String>(Menu_sincronizar.this,
		android.R.layout.simple_list_item_1, menu);*/
		//setListAdapter(adaptador);
		//getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		//lv.setAdapter(adaptador);
		
				
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
				pDialog_clientes.setProgress(0);
				pDialog_clientes.show();
				tarea2 = new MiTareaAsincronaDialog();
				tarea2.execute(proceso_clientes);
			}
			
		});
		
		bt_sincronizar_cobradores = (Button) findViewById(R.id.bt_sincronizar_cobradores);
		bt_sincronizar_cobradores.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				pDialog_cobradores.setProgress(0);
				pDialog_cobradores.show();
				tarea2 = new MiTareaAsincronaDialog();
				tarea2.execute(proceso_cobradores);
			}
		});
		
		//bt_sincronizar_cartera
		
		bt_sincronizar_cartera = (Button) findViewById(R.id.bt_sincronizar_cartera);
		bt_sincronizar_cartera.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				pDialog_cartera.setProgress(0);
				pDialog_cartera.show();
				tarea2 = new MiTareaAsincronaDialog();
				tarea2.execute(proceso_cartera);
			}
		});
		
		bt_sincronizar_usuarios= (Button) findViewById(R.id.bt_sincronizar_usuarios);
		bt_sincronizar_usuarios.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				pDialog_usuarios.setProgress(0);
				pDialog_usuarios.show();
				tarea2 = new MiTareaAsincronaDialog();
				tarea2.execute(proceso_usuarios);
			}
		});
		
		bt_sincronizar_todos = (Button) findViewById(R.id.bt_sincronizar_todos);
		bt_sincronizar_todos.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				tarea2 = new MiTareaAsincronaDialog();
				
				pDialog_clientes.setProgress(0);
				pDialog_clientes.show();
				
				//tarea2.execute(proceso_clientes);
				
				pDialog_cobradores.setProgress(0);
				pDialog_cobradores.show();
				//tarea2 = new MiTareaAsincronaDialog();
				//tarea2.execute(proceso_cobradores);
				
				pDialog_cartera.setProgress(0);
				pDialog_cartera.show();
				//tarea2 = new MiTareaAsincronaDialog();
				
				pDialog_usuarios.setProgress(0);
				pDialog_usuarios.show();
				tarea2.execute(proceso_clientes,proceso_cobradores,proceso_cartera,proceso_usuarios);
				
			}
		});
		
		
		
	}
	

	
		
    private class MiTareaAsincronaDialog extends AsyncTask<String, Integer, Boolean> {
    	
    	@Override
    	protected Boolean doInBackground(String... params) {
    		
    		if(params.length==1){
    		
	    		if(params[0].equals(proceso_clientes)    ){ 
	    			
	    			if (sincronizar_clientes()) {
						System.out.println("Clientes Sincronizados Satisfactoriamente");
						Log.i(this.getClass().toString(),"Clientes Sincronizados Satisfactoriamente");
					} else {
						System.out.println("Oops no sincronizados");
						Log.i(this.getClass().toString(), "Oops clientes no sincronizados");
					}
	    			
	    			pDialog_clientes.dismiss();
	    			//Toast.makeText(Menu_sincronizar.this, "Sincronizacion clientes Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
	    		if(params[0].equals(proceso_cartera)     ){ 
	    				
	    				if (sincronizar_cartera()) {
	    					System.out.println("Cartera Sincronizados Satisfactoriamente");
	    					Log.i(this.getClass().toString(),"Cartera Sincronizados Satisfactoriamente");
	    				} else {
	    					System.out.println("Oops no sincronizados Cartera");
	    					Log.i(this.getClass().toString(), "Oops Cartera no sincronizados");
	    				}
	        			
	        			pDialog_cartera.dismiss();		
	        			//Toast.makeText(Menu_sincronizar.this, "Sincronizacion cartera Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		if(params[0].equals(proceso_cobradores)  ){ 
	    				
	    				if (sincronizar_cobradores()) {
	    					System.out.println("Cobradores Sincronizados Satisfactoriamente");
	    					Log.i(this.getClass().toString(),"Cobradores Sincronizados Satisfactoriamente");
	    				} else {
	    					System.out.println("Oops no sincronizados Cobradores");
	    					Log.i(this.getClass().toString(), "Oops Cobradores no sincronizados");
	    				}
	    				
	    				pDialog_cobradores.dismiss();	
	    				//Toast.makeText(Menu_sincronizar.this, "Sincronizacion cobradores Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		if(params[0].equals(proceso_usuarios)  ){ 
    				
    				if (sincronizar_usuarios()) {
    					System.out.println("Usuarios Sincronizados Satisfactoriamente");
    					Log.i(this.getClass().toString(),"Usuarios Sincronizados Satisfactoriamente");
    				} else {
    					System.out.println("Oops no sincronizados Usuarios");
    					Log.i(this.getClass().toString(), "Oops Usuarios no sincronizados");
    				}
    				
    				pDialog_usuarios.dismiss();	
    				//Toast.makeText(Menu_sincronizar.this, "Sincronizacion cobradores Finalizada!", Toast.LENGTH_SHORT).show();
    		}
    		}else{
    			
    			if(params[0].equals(proceso_clientes)    ){ 
	    			
	    			if (sincronizar_clientes()) {
						System.out.println("Clientes Sincronizados Satisfactoriamente");
						Log.i(this.getClass().toString(),"Clientes Sincronizados Satisfactoriamente");
					} else {
						System.out.println("Oops no sincronizados");
						Log.i(this.getClass().toString(), "Oops clientes no sincronizados");
					}
	    			
	    			pDialog_clientes.dismiss();
	    			//Toast.makeText(Menu_sincronizar.this, "Sincronizacion clientes Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		
	    		if(params[1].equals(proceso_cobradores)  ){ 
	    				
	    				if (sincronizar_cobradores()) {
	    					System.out.println("Cobradores Sincronizados Satisfactoriamente");
	    					Log.i(this.getClass().toString(),"Cobradores Sincronizados Satisfactoriamente");
	    				} else {
	    					System.out.println("Oops no sincronizados Cobradores");
	    					Log.i(this.getClass().toString(), "Oops Cobradores no sincronizados");
	    				}
	    				
	    				pDialog_cobradores.dismiss();	
	    				//Toast.makeText(Menu_sincronizar.this, "Sincronizacion cobradores Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		if(params[2].equals(proceso_cartera)     ){ 
    				
    				if (sincronizar_cartera()) {
    					System.out.println("Cartera Sincronizados Satisfactoriamente");
    					Log.i(this.getClass().toString(),"Cartera Sincronizados Satisfactoriamente");
    				} else {
    					System.out.println("Oops no sincronizados Cartera");
    					Log.i(this.getClass().toString(), "Oops Cartera no sincronizados");
    				}
        			
        			pDialog_cartera.dismiss();		
        			//Toast.makeText(Menu_sincronizar.this, "Sincronizacion cartera Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		if(params[3].equals(proceso_cartera)     ){ 
    				
    				if (sincronizar_usuarios()) {
    					System.out.println("Cartera Sincronizados Satisfactoriamente");
    					Log.i(this.getClass().toString(),"Cartera Sincronizados Satisfactoriamente");
    				} else {
    					System.out.println("Oops no sincronizados Cartera");
    					Log.i(this.getClass().toString(), "Oops Cartera no sincronizados");
    				}
        			
        			pDialog_cartera.dismiss();		
        			//Toast.makeText(Menu_sincronizar.this, "Sincronizacion cartera Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
	    		
    		}

    		return true;
    	}
    	
    	@Override
    	public void onProgressUpdate(Integer... values) {
    		//int progreso = values[0].intValue();
    		//pDialog.setProgress(progreso);
    	}
    	
    	@Override
    	protected void onPreExecute() {
    		
    		/*pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					MiTareaAsincronaDialog.this.cancel(true);
				}
			});
    		
    		pDialog.setProgress(0);
    		pDialog.show();*/
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result) {
    		if(result)
    		{
    			//pDialog.dismiss();
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
				
				db.execSQL("delete from clientes");
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"clientes_movil/extraer_clientes");
				del.setHeader("content-type", "application/json");
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respJSON = new JSONArray(respStr);
				
				//modifcamos propiedad del progressbar
				pDialog_clientes.setMax(respJSON.length());
				
				String[] clientes = new String[respJSON.length()];
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);
					int clientes_id = obj.getInt("id");
					String cedula = obj.getString("nit");
					String nombres = obj.getString("nombres");
					String direccion = obj
							.getString("direccion");
					String telefono = obj.getString("telefono");
					String celular = obj.getString("celular");
					String sql_insert_clientes = "insert into clientes "
							+ " (clientes_id,cedula,nombres,direccion,telefono,celular) "
							+ "values" + " (" + clientes_id + ",'" + cedula
							+ "','" + nombres+ "','" + direccion + "','"
							+ telefono + "','" + celular + "') ";
					Log.i(this.getClass().toString(),sql_insert_clientes);
					db.execSQL(sql_insert_clientes);
					
					//*actualizamos barra de prograso*/
					pDialog_clientes.setProgress(i+1);
					
					//clientes[i] = "" + clientes_id +"-"+ cedula + "-"+ nombres + "-" + telefono + "-"+ celular ;
					
				}
				
				// Rellenamos la lista con los resultados
				/*ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, clientes);
				lst.setAdapter(adaptador);*/
				//mostrar_sincronizados(respJSON.length(),"Clientes sincronizados");
				
				//cerramos barra progreso
				
								
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
				db.execSQL("delete from cobradores");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"cobradores_movil/extraer_cobradores");
				del.setHeader("content-type", "application/json");
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respJSON = new JSONArray(respStr);
				
				pDialog_cobradores.setMax(respJSON.length());
				
				String[] cobradores = new String[respJSON.length()];
				
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);
					int cobradores_id = obj.getInt("id");
					String cedula = obj.getString("nit");
					String nombres = obj.getString("nombres");
					String direccion = obj
							.getString("direccion");
					String telefono = obj.getString("telefono");
					String celular = obj.getString("celular");
					String sql_insert_cobradores = "insert into cobradores "
							+ " (cobradores_id,cedula,nombres,direccion,telefono,celular) "
							+ "values" + " (" + cobradores_id + ",'" + cedula
							+ "','" + nombres
							+ "','" + direccion + "','"
							+ telefono + "','" + celular + "') ";
					Log.i(this.getClass().toString(),sql_insert_cobradores);
					db.execSQL(sql_insert_cobradores);
					
					pDialog_cobradores.setProgress(i+1);
					//cobradores[i] = "" + cobradores_id + "-"+ cedula +"-" + nombres + "-" + telefono + "-"+ celular ;
				}

				// Rellenamos la lista con los resultados
				/*ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, cobradores);
				lst.setAdapter(adaptador);*/
				//mostrar_sincronizados(respJSON.length(),"Cobradores sincronizados");
				
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
				db.execSQL("delete from cartera");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"cartera_movil/extraer_cartera");
				del.setHeader("content-type", "application/json");
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respJSON = new JSONArray(respStr);
				
				pDialog_cartera.setMax(respJSON.length());
				
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
					pDialog_cartera.setProgress(i+1);
					//cartera[i] = "" + detalle_cxc_id + "-" + creditos_id + "-" + clientes_id + "-"+ cedula ;
				}

				// Rellenamos la lista con los resultados
				/*ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, cartera);
				lst.setAdapter(adaptador);*/
				//mostrar_sincronizados(respJSON.length(),"Cartera sincronizada");
			
				
				
			}
			
			db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!", ex);
			sw = false;
		}

		return sw;

	}
	
	public boolean sincronizar_usuarios() {
		boolean sw = true;

		
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				//db.execSQL("delete from usuarios");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"usuarios_movil/extraer_usuarios");
				del.setHeader("content-type", "application/json");
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				JSONArray respJSON = new JSONArray(respStr);
				
				pDialog_usuarios.setMax(respJSON.length());
				
				String[] usuarios = new String[respJSON.length()];
				
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);
					String nombre = obj.getString("nombre");
					String clave = obj.getString("clave");
					String cobradores_id = obj.getString("cobradores_id");
					String cedula_cobrador = obj.getString("cedula_cobrador");
					
					String sql_insert_usuarios = "insert into usuarios "
							+ " (nombre,clave,cobradores_id,cedula_cobrador) "
							+ "values" + " (" + nombre + ",'"+ clave
							+ "','" + cobradores_id
							+ "','" + cedula_cobrador + "') ";
					Log.i(this.getClass().toString(),sql_insert_usuarios);
					db.execSQL(sql_insert_usuarios);
					
					pDialog_usuarios.setProgress(i+1);
					//cobradores[i] = "" + cobradores_id + "-"+ cedula +"-" + nombres + "-" + telefono + "-"+ celular ;
				}

				// Rellenamos la lista con los resultados
				/*ArrayAdapter<String> adaptador =
			    new ArrayAdapter<String>(Menu_sincronizar.this,
				android.R.layout.simple_list_item_1, cobradores);
				lst.setAdapter(adaptador);*/
				//mostrar_sincronizados(respJSON.length(),"Cobradores sincronizados");
				
			}
			
			db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!", ex);
			sw = false;
		}

		return sw;

	}
	
	
	/*funcion para mostrar en forma de alerta los clientes, cobradores y la cartera sincronizada.*/
	
	/*public void mostrar_sincronizados(int num_items,String mensaje){
			
		Toast alerta= Toast.makeText(this, mensaje+" : "+num_items, Toast.LENGTH_LONG);
		alerta.show();
		
	}*/
	
	
}
