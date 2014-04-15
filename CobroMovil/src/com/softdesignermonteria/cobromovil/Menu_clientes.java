package com.softdesignermonteria.cobromovil;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu_clientes extends Activity {
	
	private Button bt_clientes_sincoronizar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_clientes);
		Log.i(this.getClass().toString(), "Actividad Menu Clientes");			
		bt_clientes_sincoronizar = (Button)findViewById(R.id.bt_sincronizar_clientes);
		bt_clientes_sincoronizar.setOnClickListener(new OnClickListener() {  
			public void onClick(View arg0){
				    Log.i(this.getClass().toString(), "Preciona Boton Sincronizar");
			    	//setContentView(R.layout.activity_menu_clientes);
					if(sincronizar_clientes()){
						System.out.println("sincronizados satisfactoriamente");
						Log.i(this.getClass().toString(), "sincronizados satisfactoriamente");	
					}else{
						System.out.println("Ups no sincronizados");
						Log.i(this.getClass().toString(), "Ups no sincronizados");	
					}
				}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_clientes, menu);
		return true;
	}
	
	
	public boolean sincronizar_clientes(){
		boolean sw = true;
		
		//Abrimos la base de datos
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet del = new HttpGet("http://107.170.28.129/prestamos/clientes_movil/extraer_clientes");
		del.setHeader("content-type", "application/json");
		try
		{
			
			ClientesSQLiteHelper usdbh =new ClientesSQLiteHelper(this, "cobro_movil" , null, 1);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			//Si hemos abierto correctamente la base de datos
			if(db != null)
			{
				HttpResponse resp = httpClient.execute(del);
		        String respStr = EntityUtils.toString(resp.getEntity());
		        JSONArray respJSON = new JSONArray(respStr);
		        //String[] clientes = new String[respJSON.length()];
		        for(int i=0; i<respJSON.length(); i++)
		        {
		            JSONObject obj = respJSON.getJSONObject(i);
		            int clientes_id = obj.getInt("id");
		            String nombres = obj.getString("nombres");
		            String direccion_oficina = obj.getString("direccion_oficina");
		            String direccion_casa = obj.getString("direccion_casa");
		            String telefono1 = obj.getString("telefono1");
		            String  sql_insert_clientes = "insert into clientes "
		            		+ " (id,nombres,direccion_oficina,direccion_casa,telefono1) "
		            		+ "values"
		            		+ " ('"+clientes_id+"','"+nombres+"','"+direccion_oficina+"','"+direccion_casa+"','"+telefono1+"') ";
		            
		            db.execSQL(sql_insert_clientes);
		            //clientes[i] = "" + idCli + "-" + nombCli + "-" + telefCli;
		        }
		 
		        //Rellenamos la lista con los resultados
		        //ArrayAdapter<String> adaptador =
		          //      new ArrayAdapter<String>(ServicioWebRest.this,
		            //    android.R.layout.simple_list_item_1, clientes);
		        //lstClientes.setAdapter(adaptador);
			}
		        
		}
		catch(Exception ex)
		{
		        Log.e("ServicioRest","Error!", ex);
		}
		return sw;
	}

}
