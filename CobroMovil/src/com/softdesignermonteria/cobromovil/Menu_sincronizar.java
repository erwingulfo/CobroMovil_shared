package com.softdesignermonteria.cobromovil;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class Menu_sincronizar extends Activity {

	private Button bt_sincronizar_clientes;
	private Button bt_sincronizar_cobradores;
	private Button bt_sincronizar_cartera;
	private Button bt_sincronizar_usuarios;
	private Button bt_sincronizar_todos;
	private Button bt_sincronizar_recaudos;
	
	private TextView tv2;
	
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
	private String user_logueado;
	
	private String proceso_clientes = "async_clientes"; 
	private String proceso_cobradores = "async_cobradores";
	private String proceso_cartera = "async_cartera";
	private String proceso_usuarios = "async_usuarios";
	private String proceso_recaudos = "async_recaudos";
	
	private ProgressDialog pDialog_clientes;
	private ProgressDialog pDialog_cobradores;
	private ProgressDialog pDialog_cartera;
	private ProgressDialog pDialog_usuarios;
	private ProgressDialog pDialog_recaudos;
	
	private int version_database;
	public long id;
	
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_sincronizar);
		
		tv2 = (TextView) findViewById(R.id.tv7);
		
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
		
		/*Dialogo Usuarios del Sistema*/
		
		pDialog_recaudos = new ProgressDialog(Menu_sincronizar.this);
		pDialog_recaudos.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog_recaudos.setMessage("Sincronizando...Recaudos");
		pDialog_recaudos.setCancelable(true);
				
		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 * 
		 * */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		user_logueado	 = globalVariable.getUserlogueado();
		
		tv2.setText(user_logueado);
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
		
		bt_sincronizar_recaudos= (Button) findViewById(R.id.btn_subir_ecaudos);
		bt_sincronizar_recaudos.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				pDialog_recaudos.setProgress(0);
				pDialog_recaudos.show();
				tarea2 = new MiTareaAsincronaDialog();
				tarea2.execute(proceso_recaudos);
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
				
				pDialog_clientes.dismiss();
				pDialog_clientes.setProgress(0);
				pDialog_clientes.show();
				
				pDialog_recaudos.dismiss();
				pDialog_recaudos.setProgress(0);
				pDialog_recaudos.show();
				
				//tarea2.execute(proceso_clientes);
				pDialog_cobradores.dismiss();
				pDialog_cobradores.setProgress(0);
				pDialog_cobradores.show();
				
				//tarea2 = new MiTareaAsincronaDialog();
				//tarea2.execute(proceso_cobradores);
				pDialog_cartera.dismiss();
				pDialog_cartera.setProgress(0);
				pDialog_cartera.show();
				
				//tarea2 = new MiTareaAsincronaDialog();
				pDialog_usuarios.dismiss();
				pDialog_usuarios.setProgress(0);
				pDialog_usuarios.show();
				
				tarea2.execute(proceso_recaudos,proceso_clientes,proceso_cobradores,proceso_cartera,proceso_usuarios);
				
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
	    		
	    		if(params[0].equals(proceso_recaudos)     ){ 
    				
    				if (sincronizar_recaudos()) {
    					System.out.println("Recaudos Sincronizados Satisfactoriamente");
    					Log.i(this.getClass().toString(),"Recaudos Sincronizados Satisfactoriamente");
    				} else {
    					System.out.println("Oops no sincronizados Recaudos");
    					Log.i(this.getClass().toString(), "Oops Recaudos no sincronizados");
    				}
        			
        			pDialog_recaudos.dismiss();		
        			//Toast.makeText(Menu_sincronizar.this, "Sincronizacion cartera Finalizada!", Toast.LENGTH_SHORT).show();
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
    			
    			if(params[0].equals(proceso_recaudos)    ){ 
	    			
	    			if (sincronizar_recaudos()) {
						System.out.println("Recaudos Sincronizados Satisfactoriamente");
						Log.i(this.getClass().toString(),"Recaudos Sincronizados Satisfactoriamente");
					} else {
						System.out.println("Oops Recaudos no  sincronizados");
						Log.i(this.getClass().toString(), "Oops Recaudos no sincronizados");
					}
	    			
	    			pDialog_recaudos.dismiss();
	    			//Toast.makeText(Menu_sincronizar.this, "Sincronizacion clientes Finalizada!", Toast.LENGTH_SHORT).show();
	    		}
    			
    			if(params[1].equals(proceso_clientes)    ){ 
	    			
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
	    		
	    		
	    		if(params[2].equals(proceso_cobradores)  ){ 
	    				
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
	    		
	    		if(params[3].equals(proceso_cartera)     ){ 
    				
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
	    		
	    		if(params[4].equals(proceso_usuarios)     ){ 
    				
    				if (sincronizar_usuarios()) {
    					System.out.println("Usuarios Sincronizados Satisfactoriamente");
    					Log.i(this.getClass().toString(),"Usuarios Sincronizados Satisfactoriamente");
    				} else {
    					System.out.println("Oops no sincronizados Usuarios");
    					Log.i(this.getClass().toString(), "Oops Usuarios no sincronizados");
    				}
        			
        			pDialog_usuarios.dismiss();		
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

	
    
    
    
    public boolean sincronizar_recaudos() {
		boolean sw = true;
		
		String errores= "Cargados todos los Recibos Satisfactoriamente";
		int total_recibos = 0;
		int temp = 0;
		
		try {
			
		
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				
				
				
				/*calculando total de registros a actualizar*/
				String sql = "select count(*) as total from recaudos where sincronizado = 0 ";
				Log.i("Variable", "Query: "+sql);
				Cursor c1 = db.rawQuery(sql, null);
				if (c1.moveToFirst()) {
					do{
						temp = c1.getColumnIndex("total");
						total_recibos=c1.getInt(temp);
					}while(c1.moveToNext());
				}
				//modifcamos propiedad del progressbar
				pDialog_recaudos.setMax(total_recibos);
			
				String sql_recaudos; 
				String sql_recaudos_detalles;
				
				sql_recaudos = "select * from recaudos where sincronizado = 0 ";
				
				Log.i("Variable", "Query: "+sql_recaudos);
				Cursor cRecaudos = db.rawQuery(sql_recaudos, null);
				String provisional,clientes_id,cedula,creditos_id,cobradores_id,cedula_cobrador,fecha,valor_pagado_total;
				String detalle_cxc_id,valor_pagado_cuota;
				int i = 1;
			
				if (cRecaudos.moveToFirst()) {
					do{
						JSONObject encabezado = new JSONObject();
						//temp = cRecaudos.getColumnIndex("total");
						//total_recibos=c1.getInt(temp);
						temp = cRecaudos.getColumnIndex("provisional");
						provisional      = cRecaudos.getString(temp);        encabezado.put("provisional", provisional);
						temp = cRecaudos.getColumnIndex("clientes_id");
						clientes_id     = cRecaudos.getString(temp);         encabezado.put("clientes_id", clientes_id);
						temp = cRecaudos.getColumnIndex("cedula");
						cedula          = cRecaudos.getString(temp);         encabezado.put("cedula", cedula);
						temp = cRecaudos.getColumnIndex("creditos_id");
						creditos_id     = cRecaudos.getString(temp);         encabezado.put("creditos_id", creditos_id);
						temp = cRecaudos.getColumnIndex("cobradores_id");
						cobradores_id   = cRecaudos.getString(temp);         encabezado.put("cobradores_id", cobradores_id);
						temp = cRecaudos.getColumnIndex("cedula_cobrador");
						cedula_cobrador = cRecaudos.getString(temp);         encabezado.put("cedula_cobrador", cedula_cobrador);
						temp = cRecaudos.getColumnIndex("fecha");
						fecha           = cRecaudos.getString(temp);         encabezado.put("fecha", fecha);
						temp = cRecaudos.getColumnIndex("valor_pagado");
						valor_pagado_total    = cRecaudos.getString(temp);   encabezado.put("valor_pagado_total", valor_pagado_total);
						
						JSONObject detalles = new JSONObject();
						sql_recaudos_detalles = "select * from recaudos_detalles where provisional= '"+provisional+"' ";
						Cursor cRecaudosDetalles = db.rawQuery(sql_recaudos_detalles, null);
						int j = 0;
						if (cRecaudosDetalles.moveToFirst()) {
							do{
								JSONObject items = new JSONObject();
								temp = cRecaudosDetalles.getColumnIndex("detalle_cxc_id");
								detalle_cxc_id     = cRecaudosDetalles.getString(temp);        items.put("detalle_cxc_id", detalle_cxc_id);
								temp = cRecaudosDetalles.getColumnIndex("valor_pagado");
								valor_pagado_cuota = cRecaudosDetalles.getString(temp);        items.put("valor_pagado_cuota", valor_pagado_cuota);
								detalles.put(String.valueOf(j), items);
								j=j+1;
							}while(cRecaudosDetalles.moveToNext());
						}
						
						encabezado.put("detalles",detalles);
						
						System.out.println(encabezado);
						
						String jsonencabezado    = URLEncoder.encode(encabezado.toString(), "UTF-8");
						 
						 
						HttpClient httpClient = new DefaultHttpClient();
						HttpPost post = new HttpPost(url_servidor+"recibos_caja_movil/add/?encabezado="+jsonencabezado);
						post.setHeader("content-type", "application/json");
						  
						/*List<NameValuePair> postParams = new ArrayList<NameValuePair>(1);
						postParams.add(new BasicNameValuePair("encabezado", encabezado.toString() ));
						
				
						  UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
					      entity.setContentEncoding(HTTP.UTF_8);
					     
					      post.setEntity(entity);*/
						//System.out.println(entity.getContent());
						
						HttpResponse resp = httpClient.execute(post);
						System.out.println(post.getURI());
						System.out.println(resp.getParams());
						String respStr = EntityUtils.toString(resp.getEntity());
						System.out.println(respStr.getBytes());
						JSONArray respJSON = new JSONArray(respStr);
						JSONObject obj = respJSON.getJSONObject(0); 
						 
						if(obj.getBoolean("mensaje")==true){
							db.execSQL("UPDATE recaudos SET sincronizado='1' WHERE provisional= '"+provisional+"' ");
							pDialog_recaudos.setProgress(i);
							Log.i(this.getClass().toString(),"Success: "+sql_recaudos_detalles);
						}else{
							Log.i(this.getClass().toString(),"Error: "+sql_recaudos_detalles);
							//Toast.makeText(Menu_sincronizar.this, "Error Sincronizando Recibo provisional= '"+provisional+"' ", Toast.LENGTH_SHORT).show();
							pDialog_recaudos.setProgress(i);
							errores="Usp Error cargado recaudos al servidor";
						}
						
					}while(cRecaudos.moveToNext());
				}
				
				
				
			}
			
			db.close();

		} catch (Exception ex) {
			Log.e("ServicioRest", "Error!" + ex.getMessage(), ex);
			sw = false;
		}
		pDialog_recaudos.dismiss();
		return sw;
		

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
							+ "values" + " ('" + nombre + "','"+ clave
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
				//pDialog_usuarios.dismiss();
				
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
