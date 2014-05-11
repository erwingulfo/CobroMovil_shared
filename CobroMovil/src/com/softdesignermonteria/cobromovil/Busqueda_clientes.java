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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Busqueda_clientes extends Activity {
	
	private EditText nit_nombres;
	private Button consultar_clientes;
	private TextView txt;
	
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	private String parametro;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda_clientes);

		consultar_clientes = (Button) findViewById(R.id.consultar_clientes);
		consultar_clientes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Presiona Boton Buscar");
				// setContentView(R.layout.activity_menu_clientes);
				
				if (consultar_clientes()) {
					System.out
							.println("Clientes Consultados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Clientes Consultados Satisfactoriamente");
				} else {
					System.out.println("Oops clientes no encontrado");
					Log.i(this.getClass().toString(), "Oops clientes no encontrado");
				}
			}
		});
		
	}
	
	public boolean consultar_clientes() {
		boolean sw = true;
		
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			//if (db != null) {
				
				//textView2.setText("");
				
				
				//System.out.println(nit_nombres.getText());
				
				Cursor c = db.rawQuery("select * from clientes", null);
				
				if (c.moveToFirst()) {
				     //Recorremos el cursor hasta que no haya más registros
				    do{
				    	
					String id = c.getString(0);
				    String nombres = c.getString(1);
				    String direccion = c.getString(2);
				    String telefono = c.getString(3);
				    String celular = c.getString(4);
				          
				    txt.append(" " + id + " - " + nombres + " - "+ nombres +" - "+ direccion+ " - "+ telefono+ " - " + celular+"\n");
				    
				    } while(c.moveToNext());
			    
				}
					
			//}
				
			db.close();

		} catch (Exception ex) {
			Log.e("Consulta de clientes", "Error!", ex);
			sw = false;
		}

		return sw;
		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busqueda_clientes, menu);
		return true;
	}

}
