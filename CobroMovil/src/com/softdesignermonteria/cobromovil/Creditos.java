package com.softdesignermonteria.cobromovil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
					String msg2 = "";
					
					if( codigo_cobrador.getText().toString().equals("")    ){ sw=1; msg2 += " Codigo Cobrador Obligatorio"; } 
					if( auto.getText().toString().equals("")   ){ sw=1; msg2 += " Cliente es Obligatorio"; }
					if( valor_prestado.getText().toString().equals("") ){ sw=1; msg2 += " Valor Obligatorio"; }
					
					if(sw==0){ AgregarCreditos(); }else{ errorValidacion(msg2);}

				}	
			});
		
		RecibosCaja = (Button)findViewById(R.id.ButtonRecibosCaja);
		RecibosCaja.setVisibility(View.INVISIBLE);
		
		RecibosCaja.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ir a actividad descontar  martes.
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
						RecibosCaja.setVisibility(View.VISIBLE);
					}else{
						Log.i(this.getClass().toString(),"Error: "+ obj.getString("descripcion") );
						//Toast.makeText(Menu_sincronizar.this, "Error Sincronizando Recibo provisional= '"+provisional+"' ", Toast.LENGTH_SHORT).show();
						error(obj.getString("descripcion"));
						RecibosCaja.setVisibility(View.INVISIBLE);
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
