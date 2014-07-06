package com.softdesignermonteria.cobromovil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.softdesignermonteria.cobromovil.clases.ModelClientes;

import com.softdesignermonteria.cobromovil.listacobrodiario.CustomListViewListaCobroDiario;
import com.softdesignermonteria.cobromovil.listacobrodiario.ListViewCobroDiarioArrayAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Cobro_Diario extends Activity {
	
	private EditText fecha_recaudo;
	private Button ver_recaudo;
	private String url_servidor;
	private SQLiteDatabase db;
	private TextView textView1;
	private TextView sum_rec;
	
	private String nombre_database;
	private int version_database;
	private String cobradores_id;
	
	public ArrayAdapter<ModelClientes> myAdapter;
	public ListView listaCobroDiarioList;
	private Context context;
	
	
	
	DecimalFormat formateador = new DecimalFormat("###,###.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cobro__diario);
		
		context = this;
		
		String fecha = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		
		/*
		 * 
		 * Definicion y asignacion de valores globales para la app
		 * 
		 * */
		
		
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		cobradores_id	 = globalVariable.getCobradores_id();
		
		fecha_recaudo=(EditText)findViewById(R.id.fecha_recaudo);
		ver_recaudo=  (Button)findViewById(R.id.ver_recaudo);
		//lv_recaudos=  (ListView)findViewById(R.id.ListViewListaCobroDiario);
		textView1=    (TextView)findViewById(R.id.TextCodigoCobrador);
		sum_rec=      (TextView)findViewById(R.id.sum_rec);
		
		fecha_recaudo.append(fecha);
		
		
		listaCobroDiarioList = (CustomListViewListaCobroDiario)findViewById(R.id.ListViewListaCobroDiario);
		
		//ModelClientes[] ObjectItemData = new ModelClientes[0];
        
        // set the custom ArrayAdapter
        //myAdapter = new ListViewCobroDiarioArrayAdapter(context, R.layout.lista_recaudos, ObjectItemData);
        //listaCobroDiarioList.setAdapter(myAdapter);
        
        
		
		//TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this, nombre_database, null, version_database);
        //db = usdbh.getWritableDatabase();
		
        ver_recaudo.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Presiona Boton Buscar");
				// setContentView(R.layout.activity_menu_clientes);cambios nuevoss
				
				/*Limpiamos el listview*/
				/*ArrayList<String> borrar = new ArrayList<String>();
				borrar.add("");
				ArrayAdapter<String> limpiar = new ArrayAdapter<String>(Busqueda_clientes.this,
			    android.R.layout.simple_list_item_1, borrar);*/
								
				if (ver_recaudos()) {
					System.out
							.println("Recaudos Consultados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Recaudos Consultados Satisfactoriamente");
				} else {
					System.out.println("Oops recaudos no encontradow");
					Log.i(this.getClass().toString(), "Oops recaudos no encontrados");
				}
			}
		});
			
		}
		
		/*public boolean ver_recaudos(){
			//Recuperamos los valores de los campos de texto
			
			boolean sw = true;
			
			try {
				
				TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
				SQLiteDatabase db = usdbh.getWritableDatabase();
				
				if (db != null) {
					
						String sql = "select c.cedula,c.nombres,r.valor_pagado from clientes c,recaudos r " +
								" where c.cedula=r.cedula and r.cobradores_id='"+cobradores_id+"' " +
								" and r.fecha like'%"+fecha_recaudo.getText().toString()+"%'";
												
						Log.i("Variable", "Query: "+sql);
						
						String sql_count =" select count(*) as total from ("+sql+") h ";
						Cursor c1 = db.rawQuery(sql_count, null);
						
						Log.i("Variable", "Query: "+sql_count);
						
						int temp = 0;
						int total_reg = 0;
						
						
						if (c1.moveToFirst()) {
						
							do{
								temp = c1.getColumnIndex("total");
								total_reg=c1.getInt(temp);
								
							}while(c1.moveToNext());
												
						}
						
						Log.i("Variable", "Total_registros: "+total_reg);
						String[] ListadoRecaudos = new String[total_reg];
						
						Cursor c = db.rawQuery(sql, null);
						
						int cedula_temp=0;
						String cedula;
						int nombres_temp=0;
						String nombres;
						int valor_pagado_temp=0;
						double valor_pagado=0;
						double total_recaudo=0;
						int TmpIndex = 0;
						
						if(c.moveToFirst()){
												
							
						    do{
							
							cedula_temp = c.getColumnIndex("cedula");
							cedula=c.getString(cedula_temp);
							
							nombres_temp= c.getColumnIndex("nombres");
							nombres=c.getString(nombres_temp);
							
							valor_pagado_temp= c.getColumnIndex("valor_pagado");
							valor_pagado=c.getDouble(valor_pagado_temp);
														
							total_recaudo=total_recaudo+valor_pagado;
							
							//Log.i("Variable", "Cédula: "+cedula+ " nombres: "+nombres+ " Valor Pagado: "+valor_pagado+ " Total Rec: "+total_recaudo);
							
							ListadoRecaudos[TmpIndex]= "Cédula: "+cedula+" -Nombres: "+nombres+ " -Valor Pagado: "+formateador.format(valor_pagado);
							
							TmpIndex = TmpIndex +1;	
							//Log.i("Sql", "array: "+ListadoRecaudos[TmpIndex]);
													
							}while(c.moveToNext());
						    
						    
						    sum_rec.setText( formateador.format(total_recaudo) );	
						}
						
						ArrayAdapter<String> adaptador1 =
						new ArrayAdapter<String>(Cobro_Diario.this,
						android.R.layout.simple_list_item_1, ListadoRecaudos);
						lv_recaudos.setAdapter(adaptador1);
													
						}
						
						db.close();
									
			}catch (Exception ex) {
				Log.e("Consulta de clientes", "Error!", ex);
				sw = false;
			}
			return sw;
			

		}*/
				
		public boolean ver_recaudos(){
			//Recuperamos los valores de los campos de texto
			
			boolean sw = true;
			
			try {
				
				TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
				SQLiteDatabase db = usdbh.getWritableDatabase();
				
				if (db != null) {
					
						String sql = "select "
										+ "	c.cedula,c.nombres,r.valor_pagado,c.clientes_id "
										+ " from clientes c,recaudos r " 
										+ "    where c.cedula=r.cedula "
										+ "			and r.cobradores_id='"+cobradores_id+"' " 
										+ "			and r.fecha like'%"+fecha_recaudo.getText().toString()+"%'";
												
						Log.i("Variable", "Query: "+sql);
						
						Cursor cursor = db.rawQuery(sql, null);
					    int recCount = cursor.getCount();
					        
					    ModelClientes[] ObjectItemData = new ModelClientes[recCount];
					    int x = 0;
					       
						int cedula_temp=0;
						int clientes_id_temp=0;
						String cedula;
						String clientes_id;
						int nombres_temp=0;
						String nombres;
						int valor_pagado_temp=0;
						double valor_pagado=0;
						double total_recaudo=0;
						
						if(cursor.moveToFirst()){
												
							
						    do{
							
							cedula_temp = cursor.getColumnIndex("cedula");
							cedula=cursor.getString(cedula_temp);
							
							clientes_id_temp = cursor.getColumnIndex("clientes_id");
							clientes_id=cursor.getString(clientes_id_temp);
							
							nombres_temp= cursor.getColumnIndex("nombres");
							nombres=cursor.getString(nombres_temp);
							
							valor_pagado_temp= cursor.getColumnIndex("valor_pagado");
							valor_pagado=cursor.getDouble(valor_pagado_temp);
														
							total_recaudo=total_recaudo+valor_pagado;
							
							ModelClientes myObject = new ModelClientes(clientes_id,nombres,"0","0",cedula,"0",String.valueOf(valor_pagado));
 			                ObjectItemData[x] = myObject;
				            x++;
				                
													
							}while(cursor.moveToNext());
						    
						    myAdapter = new ListViewCobroDiarioArrayAdapter(context, R.layout.lista_recaudos, ObjectItemData);
				             
				            listaCobroDiarioList.setAdapter(myAdapter);
						    sum_rec.setText( formateador.format(total_recaudo) );	
						}
						
						cursor.close();
					}
						
						db.close();
									
			}catch (Exception ex) {
				Log.e("Consulta de clientes", "Error!", ex);
				sw = false;
			}
			return sw;

		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cobro__diario, menu);
		return true;
	}

}
