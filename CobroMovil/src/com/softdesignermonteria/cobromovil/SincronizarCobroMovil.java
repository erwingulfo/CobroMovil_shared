package com.softdesignermonteria.cobromovil;

import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class SincronizarCobroMovil extends Service {
	private Timer mTimer = null;
	private String url_servidor;
	private String nombre_database;
	private String user_logueado;
	private int version_database;
	private Context context;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.mTimer = new Timer();
		this.mTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			
			
			
			public void run() {
				
				final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
				url_servidor     = globalVariable.getUrl_servidor();
				nombre_database  = globalVariable.getNombre_database();
				version_database = globalVariable.getVersion_database();
				user_logueado	 = globalVariable.getUserlogueado();
				context          = globalVariable.getContext();
				ejecutarTarea();
			}
		}, 0, 1000 * 60);
	}

	private void ejecutarTarea() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				boolean sw=true;
				String errores= "Cargados todos los Recibos Satisfactoriamente";
				int total_recibos = 0;
				int temp = 0;
				try {
					
					TablasSQLiteHelper usdbh = new TablasSQLiteHelper(getApplicationContext(),nombre_database, null, version_database);
					SQLiteDatabase db2 = usdbh.getWritableDatabase();
					// Si hemos abierto correctamente la base de datos
					if (db2 != null) {
						/*calculando total de registros a actualizar*/
						String sql = "select count(*) as total from recaudos where sincronizado = 0 ";
						Log.i("Variable Sincronizar", "Query Sincronizar: "+sql);
						Cursor c1 = db2.rawQuery(sql, null);
						if (c1.moveToFirst()) {
							do{
								temp = c1.getColumnIndex("total");
								total_recibos=c1.getInt(temp);
							}while(c1.moveToNext());
						}
						c1.close();
						//modifcamos propiedad del progressbar
						if(total_recibos>0){
								String sql_recaudos; 
								String sql_recaudos_detalles;
								
								sql_recaudos = "select * from recaudos where sincronizado = 0 ";
								
								Log.i("Variable Sincronizar", "Query Sincronizar: "+sql_recaudos);
								Cursor cRecaudos = db2.rawQuery(sql_recaudos, null);
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
										Cursor cRecaudosDetalles = db2.rawQuery(sql_recaudos_detalles, null);
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
										cRecaudosDetalles.close();
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
											db2.execSQL("UPDATE recaudos SET sincronizado='1' WHERE provisional= '"+provisional+"' ");
											
											Log.i(this.getClass().toString(),"Success: "+sql_recaudos_detalles);
										}else{
											Log.i(this.getClass().toString(),"Error: "+sql_recaudos_detalles);
											//Toast.makeText(Menu_sincronizar.this, "Error Sincronizando Recibo provisional= '"+provisional+"' ", Toast.LENGTH_SHORT).show();
											
											errores="Usp Error cargado Recibos al servidor";
										}
										
									}while(cRecaudos.moveToNext());
									cRecaudos.close();
									db2.close();
								}//si recibos encabezado
						
						}//si recibos mayor a cero
						else{
							sw=false;
						}
						
						
						
					}
					
					if(sw){
						
							NotifyManager notify = new NotifyManager();
							notify.playNotification(getApplicationContext(),
									MainActivity.class, "Notificación "+errores,
								"Notificación", R.drawable.ic_action_cloud);
						
					}

					
					

				} catch (Exception ex) {
					Log.e("ServicioRest", "Error!" + ex.getMessage(), ex);
					sw = false;
				}
				
				
				
			}
		});
		t.start();
	}

}