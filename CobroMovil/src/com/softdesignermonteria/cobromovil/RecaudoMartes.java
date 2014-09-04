package com.softdesignermonteria.cobromovil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.softdesignermonteria.cobromovil.autocompleteclientes.AutocompleteCustomArrayAdapter;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteClientesTextChangedListener;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteRecaudoMartesTextChangedListener;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteView;
import com.softdesignermonteria.cobromovil.clases.ModelClientes;
import com.softdesignermonteria.cobromovil.Creditos;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RecaudoMartes extends Activity {
	
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	private String user_logueado;
	private Context context;
	
	private TextView nombre_cliente;
	private EditText codibo_cobrador;
	
	
	
	private Button guardar;
	private Button imprimir;
	
	private String provisional; 
	private String cobradores_id;
	
	
	private String cedula_cobrador;
	private String cedula_cliente;
	
	
	
	public AutoCompleteTextView auto; 
	public ArrayAdapter<ModelClientes> myAdapter;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recaudo_martes);
		
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		context = this;
		
		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor = globalVariable.getUrl_servidor();
		nombre_database = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		user_logueado = globalVariable.getUserlogueado();
		cedula_cobrador = globalVariable.getCedula_cobrador();
		cobradores_id = globalVariable.getCobradores_id();
		
 
		
		nombre_cliente = (TextView)findViewById(R.id.TextRecaudoMartesNombreCliente);
		
		codibo_cobrador = (EditText)findViewById(R.id.EditTextRecaudoMartesCorbradoresId);
		codibo_cobrador.setText(cobradores_id);
		codibo_cobrador.setEnabled(false);
		
		auto      = (CustomAutoCompleteView) findViewById(R.id.autoCompleteRecaudoMartesClientes);
		
		auto.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                 
                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                auto.setText(tv.getText().toString());
                ModelClientes temp = myAdapter.getItem(pos);
                cedula_cliente = temp.getCedula();
                 
            }

        });
         
        // add the listener so it will tries to suggest while the user types
		auto.addTextChangedListener(new CustomAutoCompleteRecaudoMartesTextChangedListener(this));
         
        // ObjectItemData has no value at first
        ModelClientes[] ObjectItemData = new ModelClientes[0];
         
        // set the custom ArrayAdapter
        myAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.lista_clientes, ObjectItemData);
        auto.setAdapter(myAdapter);
        
		
		Bundle bundle = getIntent().getExtras();
		if(!bundle.isEmpty()){
			
			System.out.println("Actividad Recaudo martes");
			System.out.println("clientes_id" + bundle.getString("clientes_id"));
			System.out.println("nombre_cliente" + bundle.getString("nombre_cliente"));
			System.out.println("cedula_cliente" + bundle.getString("cedula_cliente"));
			
			auto.setText(bundle.getString("clientes_id"));
			nombre_cliente.setText(bundle.getString("nombre_cliente"));
			cedula_cliente = (bundle.getString("cedula_cliente"));
			//auto.setEnabled(false);
		}
		
		
		guardar = (Button)findViewById(R.id.ButtonRecaudoMartesGuardar);
		imprimir = (Button)findViewById(R.id.ButtonRecaudoMartesImprimir);
		imprimir.setVisibility(View.INVISIBLE);
		
		
		
		
		
        
        guardar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				guardar(v);
				imprimir.setVisibility(View.VISIBLE);
				guardar.setVisibility(View.INVISIBLE);
			}
		});
        
        
        imprimir.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//guardar(v);
				
				Intent i = new Intent();
				i.setClass(RecaudoMartes.this, Imprimir_recibo.class);
				i.putExtra("provisional", provisional);
				Log.i("Sql", "temp:" + provisional);
				// i.putExtra("pclave_usuario", clave.getText().toString());
				startActivity(i);
				
				imprimir.setVisibility(View.INVISIBLE);
				guardar.setVisibility(View.VISIBLE);
				auto.setEnabled(true);
				auto.setText("");
				//guardar.setVisibility(View.INVISIBLE);
				
			}
		});
	
       

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recaudo_martes, menu);
		return true;
	}
	

	public ModelClientes[] Read(String valor_filtro) {
		// TODO Auto-generated constructor stub
		
		  TablasSQLiteHelper  t =  new TablasSQLiteHelper (context, nombre_database, null, version_database);
          ModelClientes[] myObjs = t.ObtenerTodosClientes(context, valor_filtro.toString());
          
          return myObjs;
	}
	
	
	public void guardar(View v){
		
		
			JSONObject dato = new JSONObject();
			
			try {
				dato.put("clientes_id", auto.getText().toString());
				
				TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
				SQLiteDatabase db = usdbh.getWritableDatabase();
				
				String jsonencabezado;
				try {

					jsonencabezado = URLEncoder.encode(dato.toString(), "UTF-8");
					
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost post = new HttpPost(url_servidor + "cartera_movil/extraer_cartera_martes/?clientes="+jsonencabezado);
					post.setHeader("content-type", "application/json");
					
					HttpResponse resp;
					try {
						resp = httpClient.execute(post);
					
						Log.i(this.getClass().toString(),"Se envio al Cliente a guadar" + jsonencabezado );

						System.out.println(post.getURI());
						System.out.println(resp.getParams());
						
						String respStr = EntityUtils.toString(resp.getEntity());
						
						System.out.println(respStr.getBytes());
						
						JSONArray respJSON = new JSONArray(respStr);
						
						JSONObject obj = respJSON.getJSONObject(0); 
						
						String creditos_id = obj.getString("creditos_id");
						
						
						if (db != null) {

							String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
									.format(new Date());
							/*
							 * Se Genera Codigo provisional
							 */
							provisional = UUID.randomUUID().toString();
							Log.i("Sql", "temp:" + provisional);
							
							double valor_recaudado = 0;
							
							for (int i = 0; i < respJSON.length(); i++) {
								obj       = respJSON.getJSONObject(i);
								valor_recaudado += obj.getDouble("valor");
							}
							
							if(valor_recaudado==0){
								
								errorValidacion("No se encontro ningun martes a descontar");
							
							}else{
			
										String insert_recaudos = "insert into recaudos"
												+ " (provisional,clientes_id,cedula,creditos_id,cobradores_id,cedula_cobrador,fecha,valor_pagado,saldo) "
												+ " values ('" + provisional + "','"
												+ auto.getText() + "','" + cedula_cliente
												+ "','" + creditos_id + "'," + "'"
												+ cobradores_id + "','" + cedula_cobrador
												+ "','" + fecha + "','" + valor_recaudado + "','0') ";
			
										db.execSQL(insert_recaudos);
							
										String insert_recaudos_detalles;
												for (int i = 0; i < respJSON.length(); i++) {
														obj       = respJSON.getJSONObject(i);
														insert_recaudos_detalles = "insert into recaudos_detalles "
																+ "	(provisional,detalle_cxc_id,valor_pagado) "
																+ " values('"
																+ provisional
																+ "','"
																+ obj.getString("detalle_cxc_id")
																+ "','"
																+ obj.getString("valor")
																+ "')";
														
															Log.e("Sql", "Sentencia:" + insert_recaudos_detalles);
					
															db.execSQL(insert_recaudos_detalles);
												}
			
										Log.e("Sql", "Sentencia:" + insert_recaudos);
										
										/*actualizando saldo credito y valor pagado*/
										String sqlsaldo = " select "
														+ " sum((valor-IFNULL((select sum(rd.valor_pagado) from recaudos_detalles rd where rd.detalle_cxc_id=c.detalle_cxc_id),0))) as saldo "
														+ " from cartera c "
														+ "	where c.creditos_id= '" + creditos_id + "' ";
										String sqlpagado = " select sum(valor_pagado) "
												+ " from recaudos_detalles " + "	where "
												+ " provisional='" + provisional + "' ";
										
										String sqlUpdateRecaudo = " update recaudos set "
																+ " valor_pagado = IFNULL( ("+sqlsaldo+") ,0), "
																+ " saldo        = IFNULL( ("+sqlpagado+") ,0) "
																+ " where  "
																+ " provisional='" + provisional + "' ";
										db.execSQL(sqlUpdateRecaudo);
										Log.i(this.getClass().toString() + "Recaudos", "Recaudo actualizado " + sqlUpdateRecaudo);
										//Cursor csaldo = db.rawQuery(sqlsaldo, null);
						
							}

						}	
						
						db.close();
						

						
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("Sql", "JSONException:" + e.getMessage());
				e.printStackTrace();
				
			}
			

			if(!SincronizaCreditos(auto.getText().toString())){
				errorValidacion("Sincronice Cartera nuevamente");
				Log.i(this.getClass().toString() + "Recaudo Martes", "Recaudo Martes No Sincronizado.. cartera no sincronizada");
			}else{
				Log.i(this.getClass().toString() + "Recaudo Martes", "Recaudo Martes Sincronizado cartera sincronizada para este cliente");
			}
		
	}
	
	
	
	public boolean SincronizaCreditos(String clientesId) {
		boolean sw = true;
		Log.i(this.getClass().toString()," Sincronizando credito " + clientesId );
		try {
			
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				db.execSQL("delete from cartera where clientes_id = '"+clientesId+"' ");
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"cartera_movil/extraer_cartera/?clientes_id="+clientesId);
				del.setHeader("content-type", "application/json");
				
				System.out.println(del.getURI());
				System.out.println(del.getParams());
				HttpResponse resp = httpClient.execute(del);
			
				String respStr = EntityUtils.toString(resp.getEntity());
				System.out.println(respStr.getBytes());
				JSONArray respJSON = new JSONArray(respStr);
				
				
				
				String[] cartera = new String[respJSON.length()];
				
				for (int i = 0; i < respJSON.length(); i++) {
					JSONObject obj = respJSON.getJSONObject(i);
					
					int detalle_cxc_id   = obj.getInt("detalle_cxc_id");
					int cobradores_id    = obj.getInt("cobradores_id");
					int creditos_id      = obj.getInt("creditos_id");
					int clientes_id      = obj.getInt("clientes_id");
					double valor         = obj.getDouble("valor");
					double total_credito = obj.getDouble("total_credito");
					
					String vencimiento     = obj.getString("vencimiento");
					String cedula          = obj.getString("cedula");
					String cedula_cobrador = obj.getString("cedula_cobrador");
					
					
					String sql_insert_caretra = "insert into cartera "
							+ " (detalle_cxc_id,creditos_id,clientes_id,cedula,cobradores_id,cedula_cobrador,vencimiento,valor,total_credito) "
							+ "values ( '"+ detalle_cxc_id +"','"+ creditos_id +"','"+ clientes_id +"','"+ cedula +"','"+ cobradores_id +"','"+ cedula_cobrador +"','"+ vencimiento +"','"+ valor +"','"+total_credito+"') "; 
								
					Log.i(this.getClass().toString(),sql_insert_caretra);
					db.execSQL(sql_insert_caretra);
					
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
			Log.e("ServicioRest", "Error!" + ex.getMessage().toString());
			sw = false;
		}

		return sw;

	}
	
	
	
	public void success(String msg) {
		Toast toast1 = Toast.makeText(this, "Cliente Agregado. Actualice su Cartera", Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast1.show();
    }

    public void error(String msg) {
		Toast toast2 = Toast.makeText(this, "Error. Cliente no Agregado" + msg, Toast.LENGTH_SHORT);
        toast2.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast2.show();
    }
    
    public void errorValidacion(String msg) {
		Toast toast3 = Toast.makeText(this, "Error." + msg, Toast.LENGTH_SHORT);
        toast3.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast3.show();
    }
	

}
