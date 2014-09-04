package com.softdesignermonteria.cobromovil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;

import com.softdesignermonteria.cobromovil.autocompleteclientes.AutocompleteCustomArrayAdapter;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteClientesTextChangedListener;
import com.softdesignermonteria.cobromovil.autocompleteclientes.CustomAutoCompleteView;
import com.softdesignermonteria.cobromovil.clases.ModelClientes;

public class Clientes extends Activity {

	private Button enviar;
	private EditText cedula;
	private EditText nombre1;
	private EditText nombre2;
	private EditText apellido1;
	private EditText apellido2;

	private EditText direccion;
	private EditText telefono;
	private TextView nombre_referencia;
	public AutoCompleteTextView auto; 
	

	private String Msg = "";

	private String url_servidor;
	private String nombre_database;
	private int version_database;
	
	private Context context;
	
	public ArrayAdapter<ModelClientes> myAdapter;
	
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
		
		context = this;

		/**
		 * Asignacion de valores de variables globales android se especifica
		 * nombre de la base de datos version de la base de datos url de los
		 * webservices
		 */

		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

		url_servidor = globalVariable.getUrl_servidor();
		nombre_database = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();

		cedula    = (EditText) findViewById(R.id.TextClientesCedula);
		nombre1   = (EditText) findViewById(R.id.TextClientesNombre1);
		nombre2   = (EditText) findViewById(R.id.TextClkientesNombre2);
		apellido1 = (EditText) findViewById(R.id.TxtClientesApellido1);
		apellido2 = (EditText) findViewById(R.id.TextClientesApellido2);
		direccion = (EditText) findViewById(R.id.TextClientesDireccion);
		telefono  = (EditText) findViewById(R.id.TextClientesTelefono);
		nombre_referencia = (TextView) findViewById(R.id.TextClientesNombreReferencia);
		
		auto      = (CustomAutoCompleteView) findViewById(R.id.autoCompleteClientesReferencia);
		
		auto.setOnItemClickListener(new OnItemClickListener() {
			 
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                 
                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                auto.setText(tv.getText().toString());
                
                TextView tv1 = (TextView) rl.getChildAt(1);
                nombre_referencia.setText(tv1.getText().toString());
                 
            }

        });
         
        // add the listener so it will tries to suggest while the user types
		auto.addTextChangedListener(new CustomAutoCompleteClientesTextChangedListener(this));
         
        // ObjectItemData has no value at first
        ModelClientes[] ObjectItemData = new ModelClientes[0];
         
        // set the custom ArrayAdapter
        myAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.lista_clientes, ObjectItemData);
        auto.setAdapter(myAdapter);
	
	

		enviar = (Button) findViewById(R.id.ButtonClientesEnviar);

		enviar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

					int sw=0;
					int sw2=1;
					String msg2 = "";
					
					
					if( cedula.getText().toString().equals("")    ){ sw=1; msg2 += " Cedula Obligatorio"; } 
					if( nombre1.getText().toString().equals("")   ){ sw=1; msg2 += " Nombre1 Obligatorio"; }
					if( apellido1.getText().toString().equals("") ){ sw=1; msg2 += " Apellido1 Obligatorio"; }
					if( direccion.getText().toString().equals("") ){ sw=1; msg2 += " Direccion Obligatorio"; }
					if( telefono.getText().toString().equals("")  ){ sw=1; msg2 += " Telefono Obligatorio"; }
					if( auto.getText().toString().equals("")      ){ sw=1; msg2 += " Referencia Obligatoria"; }
					
					
					if(sw==0 ){ AgregarClientes(); sw2=0; }else{ errorValidacion(msg2);}
					

				}	
			});

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
    

	public void AgregarClientes() {
		// Construimos el objeto cliente en formato JSON
		JSONObject dato = new JSONObject();
		Log.e(this.getClass().toString() + "Clientes.java Informacion", "Entro en Agregar Clientes");
		try {

			dato.put("cedula", cedula.getText().toString());
			dato.put("nombre1", nombre1.getText().toString());
			dato.put("nombre2", nombre2.getText().toString());
			dato.put("apellido1", apellido1.getText().toString());
			dato.put("apellido2", apellido2.getText().toString());
			dato.put("direccion", direccion.getText().toString());
			dato.put("telefono", telefono.getText().toString());
			dato.put("referencia_id", auto.getText().toString());
			

			StringEntity entity;
			try {
				
				
				
				String jsonencabezado    = URLEncoder.encode(dato.toString(), "UTF-8");
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(url_servidor + "clientes_movil/add/?clientes="+jsonencabezado);
				post.setHeader("content-type", "application/json");
				
				//entity = new StringEntity(dato.toString());

				//post.setEntity(entity);

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
					 
					if(obj.getBoolean("mensaje")==true){
						//db.execSQL("UPDATE recaudos SET sincronizado='1' WHERE provisional= '"+provisional+"' ");
						success(obj.getString("descripcion"));
						Log.i(this.getClass().toString(),"Success: "+ obj.getString("descripcion") );
						
						Log.i(this.getClass().toString(),"Sincronizamos este cliente en particular" + cedula.getText().toString());
						 
						if(!SincronizaClientes( cedula.getText().toString() )){
							error("Error sincronizando este clientes hagalo manualmente");
							Log.i(this.getClass().toString(),"Error sincronizando este cliente en particular" + cedula.getText().toString());
						}else{
							Log.i(this.getClass().toString(),"sincronizado este cliente en particular" + cedula.getText().toString());
							success("Cliente Sincronizado en el dispositivo movil");
						}
						
					}else{
						Log.i(this.getClass().toString(),"Error: "+ obj.getString("descripcion") );
						//Toast.makeText(Menu_sincronizar.this, "Error Sincronizando Recibo provisional= '"+provisional+"' ", Toast.LENGTH_SHORT).show();
						error(obj.getString("descripcion"));
						
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
	
	
	public ModelClientes[] Read(String valor_filtro) {
		// TODO Auto-generated constructor stub
		
		  TablasSQLiteHelper  t =  new TablasSQLiteHelper (context, nombre_database, null, version_database);
          ModelClientes[] myObjs = t.ObtenerTodosClientes(context, valor_filtro.toString());
          
          return myObjs;
	}
	
	
	public boolean SincronizaClientes(String Cedula) {
		boolean sw = true;
		
		Log.e(this.getClass().toString() + "Clientes.java Informacion", "Entro en SincronizaClientes");
		
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			if (db != null) {
				
				db.execSQL("delete from clientes where cedula = '"+Cedula+"' ");
				
				HttpClient httpClient = new DefaultHttpClient();
				HttpGet del = new HttpGet(url_servidor+"clientes_movil/extraer_clientes/?nit="+Cedula);
				del.setHeader("content-type", "application/json");
				
				System.out.println(del.getURI());
				System.out.println(del.getParams());
				
				HttpResponse resp = httpClient.execute(del);
				String respStr = EntityUtils.toString(resp.getEntity());
				System.out.println(respStr.getBytes());
				JSONArray respJSON = new JSONArray(respStr);
				
				//modifcamos propiedad del progressbar
				
				int clientes_id = 0;
				String cedula = "";
				String nombres = "";
				String direccion = "";
				String telefono = "";
				String celular = "";
				String referencia_id = "";
				String sql_insert_clientes = "";
				
				
				
				for (int i = 0; i < respJSON.length(); i++) {
					
					JSONObject obj       = respJSON.getJSONObject(i);
					clientes_id          = obj.getInt("id");
					cedula               = obj.getString("nit");
					nombres              = obj.getString("nombres");
					direccion            = obj.getString("direccion");
					telefono             = obj.getString("telefono");
					celular              = obj.getString("celular");
					referencia_id        = obj.getString("referencia_id");
				
					sql_insert_clientes  = "insert into clientes "
											+ " ( clientes_id"
											+ "   ,cedula"
											+ "   ,nombres"
											+ "   ,direccion"
											+ "   ,telefono"
											+ "   ,celular"
											+ "   ,referencia_id"
											+ " ) "
											+ "   values" 
											+ " (" 
											+ "    '" + clientes_id   + "'"
											+ "   ,'" + cedula        + "'"
											+ "   ,'" + nombres       + "'"
											+ "   ,'" + direccion     + "'"
											+ "   ,'" + telefono      + "'"
											+ "   ,'" + celular       + "'"
											+ "   ,'" + referencia_id + "'"
											+ "  ) ";
					
					Log.i(this.getClass().toString(),sql_insert_clientes);
					db.execSQL(sql_insert_clientes);
					
					
				}
				
			}
			
			db.close();

		} catch (Exception ex) {
			Log.e(this.getClass().toString() + "Clientes.java Informacion", "Error!", ex);
			sw = false;
		}

		return sw;
	
	
	}
	
		
		
}


