package com.softdesignermonteria.cobromovil;

import java.util.ArrayList;

import android.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Busqueda_clientes extends Activity {
	
	private EditText nit_nombres;
	private Button consultar_clientes;
	private Button limpiar;
	private EditText txt;
	
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	private String parametro;
	public ArrayList<String> menu = new ArrayList<String>();
	public ArrayList<String> adaptador = new ArrayList<String>();
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda_clientes);
	
		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 * */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		
		txt = (EditText)findViewById(R.id.txt);
		nit_nombres=(EditText)findViewById(R.id.nit_nombres);

		consultar_clientes = (Button) findViewById(R.id.consultar_clientes);
		consultar_clientes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Presiona Boton Buscar");
				// setContentView(R.layout.activity_menu_clientes);cambios nuevoss
				
				/*Limpiamos el listview*/
				/*ArrayList<String> borrar = new ArrayList<String>();
				borrar.add("");
				ArrayAdapter<String> limpiar = new ArrayAdapter<String>(Busqueda_clientes.this,
			    android.R.layout.simple_list_item_1, borrar);*/
				
				
				if (consultar_clientes()) {
					System.out
							.println("Clientes Consultados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Clientes Consultados Satisfactoriamente");
				} else {
					System.out.println("Oops clientes no encontrado");
					Log.i(this.getClass().toString(), "Oops clientes no encontrados");
				}
			}
		});
		
		
		
	}
	
	public boolean consultar_clientes() {
		boolean sw = true;
		
		
		parametro=nit_nombres.getText().toString();
			
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			
								
			if (db != null) {
				
					Cursor c = db.rawQuery("select * from clientes where cedula like"+"'%"+parametro+"%'" +
						 	   "or nombres like"+"'%"+parametro+"%'", null);
					
					String cadena="select * from clientes where cedula="+"'"+parametro+"'";
					
					Log.i("Sql", "Sentencia:"+cadena);
															
					if (c.moveToFirst()) {
					     //Recorremos el cursor hasta que no haya más registros
						do{
					
						//Log.i("Sql", "pos"+pos+":"+pos);
							
						String id = c.getString(0);
						String cedula = c.getString(1);
					    String nombres = c.getString(2);
					    String direccion = c.getString(3);
					    String telefono = c.getString(4);
					    String celular = c.getString(5);
					    
					    					    					    					    
					    txt.append(" " + id +" - "+ cedula +" - " + nombres + " - "+ direccion+ " - "+ telefono+ " - " + celular+"\n");
					    
					    ArrayAdapter<String> adaptador =
					    		new ArrayAdapter<String>(Busqueda_clientes.this,
					    android.R.layout.simple_list_item_1, menu);
					    //lv.setAdapter(adaptador);
					    
					    					    
					    } while(c.moveToNext());
										
				    }else{
				    	txt.append("Cliente no existe");
				    }
				  
				 
				}
				
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
