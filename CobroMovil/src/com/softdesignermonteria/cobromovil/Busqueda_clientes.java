package com.softdesignermonteria.cobromovil;

import java.util.ArrayList;


import com.softdesignermonteria.cobromovil.clases.ModelClientes;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.softdesignermonteria.cobromovil.TablasSQLiteHelper;
import com.softdesignermonteria.cobromovil.listaclientes.*;



public class Busqueda_clientes extends Activity {
	
	private EditText nit_nombres;
	private Button consultar_clientes;
	private TextView textuserlogueado;
	private String user_logueado;
	
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	public ArrayList<String> menu = new ArrayList<String>();
	public ArrayList<String> adaptador = new ArrayList<String>();
	
	public ArrayAdapter<ModelClientes> myAdapter;
	public ListView listaclientesList;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_busqueda_clientes);
		final Context context = this;
		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 * */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		user_logueado	 = globalVariable.getUserlogueado();
		
		textuserlogueado = (TextView)findViewById(R.id.BusquedaClientesUserLogueado);
		textuserlogueado.setText(user_logueado);
		
		nit_nombres=(EditText)findViewById(R.id.nit_nombres);
		
		listaclientesList = (CustomListViewInfoClientes) findViewById(R.id.listViewListaClientes);
		
		ModelClientes[] ObjectItemData = new ModelClientes[0];
        
        // set the custom ArrayAdapter
        myAdapter = new ListViewInfoClientesArrayAdapter(context, R.layout.info_clientes, ObjectItemData);
        listaclientesList.setAdapter(myAdapter);
		
		
		

		consultar_clientes = (Button) findViewById(R.id.consultar_clientes);
		consultar_clientes.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Presiona Boton Buscar");
				
		        
				    String userInput = nit_nombres.getText().toString();
		        
		          // if you want to see in the logcat what the user types
		            Log.e(this.getClass().toString(), "User input: " + userInput);
		     
		            TablasSQLiteHelper  t =  new TablasSQLiteHelper (context, nombre_database, null, version_database);
		            ModelClientes[] myObjs = t.ObtenerTodosClientes(context, userInput.toString());

		            myAdapter = new ListViewInfoClientesArrayAdapter(context, R.layout.info_clientes, myObjs);
		             
		            listaclientesList.setAdapter(myAdapter);
		            
		            Log.e(this.getClass().toString(), "despues de lista clientes");
				
			
			}
		});
		
		
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.busqueda_clientes, menu);
		return true;
	}
	
	

}
