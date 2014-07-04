package com.softdesignermonteria.cobromovil;

import java.util.ArrayList;







import com.softdesignermonteria.cobromovil.clases.ModelClientes;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
	private String cobradores_id;
	
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
		cobradores_id    = globalVariable.getCobradores_id();
		
		Log.e("Busqueda Clientes","cobradores_id = " + cobradores_id);
		
		textuserlogueado = (TextView)findViewById(R.id.BusquedaClientesUserLogueado);
		textuserlogueado.setText(user_logueado);
		
		nit_nombres=(EditText)findViewById(R.id.nit_nombres);
		
		listaclientesList = (CustomListViewInfoClientes) findViewById(R.id.listViewListaClientes);
		
		ModelClientes[] ObjectItemData = new ModelClientes[0];
        
        // set the custom ArrayAdapter
        myAdapter = new ListViewInfoClientesArrayAdapter(context, R.layout.info_clientes, ObjectItemData);
        listaclientesList.setAdapter(myAdapter);
        
        listaclientesList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int posicion,
					long arg3) {
				// TODO Auto-generated method stub
				Log.i(this.getClass().toString(), "Item Seleccionado posicion "+posicion);
				final ModelClientes temp = myAdapter.getItem(posicion);
				
				Log.i(this.getClass().toString(), "Item Seleccionado posicion Clientes _id "+temp.getClientes_id());
				Log.i(this.getClass().toString(), "Item Seleccionado posicion getCedula    "+temp.getCedula());
				Log.i(this.getClass().toString(), "Item Seleccionado posicion getDireccion _id "+temp.getDireccion());
				Log.i(this.getClass().toString(), "Item Seleccionado posicion getNombre _id "+temp.getNombre());
				Log.i(this.getClass().toString(), "Item Seleccionado posicion getTelefono _id "+temp.getTelefono());
				
				final CharSequence[] items = {"Generar Credito", "Recaudo Martes"};
				 
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Escoja Una Opcion");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				        //Toast toast = Toast.makeText(getApplicationContext(), "Haz elegido la opcion: " + items[item] , Toast.LENGTH_SHORT);
				        //toast.show();
				        
				    	if(items[item].equals("Generar Credito")){
					    	Intent i = new Intent();
							i.setClass(Busqueda_clientes.this, Creditos.class);
					        
					        i.putExtra("clientes_id", temp.getClientes_id());
					        i.putExtra("nombre_cliente", temp.getNombre());
					        startActivity(i);
				    	}
				        
				    	if(items[item].equals("Recaudo Martes")){
					    	Intent i = new Intent();
							i.setClass(Busqueda_clientes.this, RecaudoMartes.class);
					        
					        i.putExtra("clientes_id", temp.getClientes_id());
					        i.putExtra("nombre_cliente", temp.getNombre());
					        startActivity(i);
				    	}
				    	
				        dialog.cancel();
				        
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();
				
				
				
			}
        	
		});
		
		/*listaclientesList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent();
				i.setClass(Busqueda_clientes.this, MenuPrincipal.class);
		        //i.putExtra("cobradores_id", usuario.getText().toString());
		        //i.putExtra("pclave_usuario", clave.getText().toString());
		        
		        startActivity(i);
				
			}
		});*/
		

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
