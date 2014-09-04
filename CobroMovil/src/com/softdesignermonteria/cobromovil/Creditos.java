package com.softdesignermonteria.cobromovil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.softdesignermonteria.cobromovil.autocompleteclientes.AutocompleteCustomArrayAdapter;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteClientesTextChangedListener;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteCreditosTextChangedListener;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteView;
import com.softdesignermonteria.cobromovil.clases.ModelClientes;
import com.softdesignermonteria.cobromovil.listaclientes.CustomListViewInfoClientes;
import com.softdesignermonteria.cobromovil.listaclientes.ListViewInfoClientesArrayAdapter;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Creditos extends Activity {
	

	private TextView nombre_cliente;
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	private String cobradores_id;
	
	private Button enviar;
	private Button RecibosCaja;
	private String cedula_clientes;
	
	public ArrayList<String> menu = new ArrayList<String>();
	public ArrayList<String> adaptador = new ArrayList<String>();
	
	public ArrayAdapter<ModelClientes> myAdapter;
	public ListView listaclientesList;
	public AutoCompleteTextView auto;
	private EditText codigo_cobrador,valor_prestado;
	private Context context;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_creditos);
		
		context = this;
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy =
			new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		
		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 * */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		
		cobradores_id    = globalVariable.getCobradores_id();
		
		Log.e("Creditos ","cobradores_id = " + cobradores_id);
				
		nombre_cliente = (TextView)findViewById(R.id.TextViewCreditosNombreCliente);
		codigo_cobrador = (EditText)findViewById(R.id.EditTextCodigoCobrador);
		valor_prestado  = (EditText)findViewById(R.id.CreditosValorAPrestar);
		
		auto      = (CustomAutoCompleteView) findViewById(R.id.autoCompleteCreditosClientes);
		
		Bundle bundle = getIntent().getExtras();
		if(!bundle.isEmpty()){
			auto.setText(bundle.getString("clientes_id"));
			nombre_cliente.setText(bundle.getString("nombre_cliente"));
			auto.setEnabled(false);
		}
		
		codigo_cobrador.setText(cobradores_id);
		codigo_cobrador.setEnabled(false);
		
		auto.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                 
                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                auto.setText(tv.getText().toString());
                
                TextView tv1 = (TextView) rl.getChildAt(1);
                nombre_cliente.setText(tv1.getText().toString());
                
                ModelClientes temp = myAdapter.getItem(pos);
                cedula_clientes = temp.getCedula();
                 
            }

        });
         
        // add the listener so it will tries to suggest while the user types
		auto.addTextChangedListener(new CustomAutoCompleteCreditosTextChangedListener(this));
         
        // ObjectItemData has no value at first
        ModelClientes[] ObjectItemData = new ModelClientes[0];
         
        // set the custom ArrayAdapter
        myAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.lista_clientes, ObjectItemData);
        auto.setAdapter(myAdapter);
		
        enviar = (Button) findViewById(R.id.ButtonCreditosAgregar);
		enviar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					int sw=0;
					int sw2=1;
					String msg2 = "";
					
					
					if( codigo_cobrador.getText().toString().equals("")    ){ sw=1; msg2 += " Codigo Cobrador Obligatorio"; } 
					if( auto.getText().toString().equals("")   ){ sw=1; msg2 += " Cliente es Obligatorio"; }
					if( valor_prestado.getText().toString().equals("") ){ sw=1; msg2 += " Valor Obligatorio"; }
					
					if(sw==0){ AgregarCreditos(); sw=0; }else{ errorValidacion(msg2);}
					

				}	
			});
		
		RecibosCaja = (Button)findViewById(R.id.ButtonRecibosCaja);
		RecibosCaja.setVisibility(View.INVISIBLE);
		
		RecibosCaja.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ir a actividad descontar  martes.
				System.out.println("click en Recibos Caja Martes");
				System.out.println("clientes_id" + auto.getText().toString());
				System.out.println("nombre_cliente" + nombre_cliente.getText().toString());
				System.out.println("cedula_cliente" + cedula_clientes);
				
				Intent i = new Intent();
				i.setClass(Creditos.this, RecaudoMartes.class);
		        
		        i.putExtra("clientes_id", auto.getText().toString());
		        i.putExtra("nombre_cliente", nombre_cliente.getText().toString() );
		        i.putExtra("cedula_cliente", cedula_clientes);
		        startActivity(i);
		        
		        
		        
		        
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.creditos, menu);
		return true;
	}
	
	

	public void AgregarCreditos() {
		// Construimos el objeto cliente en formato JSON
		JSONObject dato = new JSONObject();

		try {

			dato.put("cobradores_id", codigo_cobrador.getText().toString());
			dato.put("clientes_id", auto.getText().toString());
			dato.put("valor", valor_prestado.getText().toString());
			
			StringEntity entity;
			try {
				
				
				
				String jsonencabezado    = URLEncoder.encode(dato.toString(), "UTF-8");
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(url_servidor + "creditos_movil/add/?creditos="+jsonencabezado);
				post.setHeader("content-type", "application/json");
				
				//entity = new StringEntity(dato.toString());

				//post.setEntity(entity);

				HttpResponse resp;
				try {
					resp = httpClient.execute(post);
					Log.i(this.getClass().toString(),"Se envio al Credito a guadar" + jsonencabezado );

					System.out.println(post.getURI());
					System.out.println(resp.getParams());
					String respStr = EntityUtils.toString(resp.getEntity());
					System.out.println(respStr.getBytes());
					JSONArray respJSON = new JSONArray(respStr);
					JSONObject obj = respJSON.getJSONObject(0); 
					 
					if(obj.getBoolean("mensaje")==true){
						//db.execSQL("UPDATE recaudos SET sincronizado='1' WHERE provisional= '"+provisional+"' ");
						success(obj.getString("descripcion"));
						Log.i(this.getClass().toString(),"Success: "+ obj.getString("descripcion") );
						if(!SincronizaCreditos(auto.getText().toString())){
							Log.i(this.getClass().toString(),"Error sincronizando credito de este clientes " +  auto.getText().toString() );
							error("Sincronize la cartera manualmente");
						}else{
							success("Cartera Sincronizada satisfactoriamente");
							Log.i(this.getClass().toString(),"sincronizando credito de este clientes " +  auto.getText().toString() );
						}
						RecibosCaja.setVisibility(View.VISIBLE);
						enviar.setVisibility(View.INVISIBLE);
					}else{
						Log.i(this.getClass().toString(),"Error: "+ obj.getString("descripcion") );
						//Toast.makeText(Menu_sincronizar.this, "Error Sincronizando Recibo provisional= '"+provisional+"' ", Toast.LENGTH_SHORT).show();
						error(obj.getString("descripcion"));
						RecibosCaja.setVisibility(View.INVISIBLE);
						enviar.setVisibility(View.VISIBLE);
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
		Toast toast = Toast.makeText(this, "Cliente Agregado. Actualice su Cartera", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    public void error(String msg) {
		Toast toast = Toast.makeText(this, "Error. Cliente no Agregado" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
    
    public void errorValidacion(String msg) {
		Toast toast = Toast.makeText(this, "Error." + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
    
	
    public ModelClientes[] Read(String valor_filtro) {
		// TODO Auto-generated constructor stub
		
		  TablasSQLiteHelper  t =  new TablasSQLiteHelper (context, nombre_database, null, version_database);
          ModelClientes[] myObjs = t.ObtenerTodosClientes(context, valor_filtro.toString());
          
          return myObjs;
	}


}
