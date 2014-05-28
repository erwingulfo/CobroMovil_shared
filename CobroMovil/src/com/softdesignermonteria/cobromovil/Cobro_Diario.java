package com.softdesignermonteria.cobromovil;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Templates;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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
	private TextView userlogueado;
	private ListView lv_recaudos;
	private String nombre_database;
	private int version_database;
	private String cobradores_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cobro__diario);
		
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		
		/*
		 * 
		 * Definicion y asignacion de valores globales para la app
		 * 
		 * */
		globalVariable.setNombre_database("cobro_movil");
		globalVariable.setVersion_database(10);
		globalVariable.setUrl_servidor("http://inversionesjd.dydsoluciones.net/");
		
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		cobradores_id	 = globalVariable.getCobradores_id();
		
		fecha_recaudo=(EditText)findViewById(R.id.fecha_recaudo);
		ver_recaudo=  (Button)findViewById(R.id.ver_recaudo);
		lv_recaudos=  (ListView)findViewById(R.id.lv_recaudos);
		
		TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this, nombre_database, null, version_database);
        db = usdbh.getWritableDatabase();
		
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
		
		public boolean ver_recaudos(){
			//Recuperamos los valores de los campos de texto
			
			String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			boolean sw = true;
			
			String f_recaudo = fecha_recaudo.getText().toString();
			
			try {
				
				TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
				SQLiteDatabase db = usdbh.getWritableDatabase();
				
				if (db != null) {
					
						Cursor c = db.rawQuery("select c.cedula,c.nombres,r.valor_pagado from clientes c,recaudos r " +
								" where c.cedula=r.cedula and r.cobradores_id='"+cobradores_id+"'", null);
						
						String sql="select c.cedula,c.nombres,r.valor_pagado from clientes c,recaudos r " +
								" where c.cedula=r.cedula and r.cobradores_id='"+cobradores_id+"'";
						
						Log.i("Variable", "Query: "+sql);
						
						if(c.moveToFirst()){
							
							int cedula_temp=0;
							String cedula;
							int nombres_temp=0;
							String nombres;
							int valor_pagado_temp=0;
							String valor_pagado;
							int total_recaudo=0;
								
							int TmpIndex = 0;	
															
							do{
							
							cedula_temp = c.getColumnIndex("cedula");
							cedula=c.getString(cedula_temp);
							
							nombres_temp= c.getColumnIndex("nombres");
							nombres=c.getString(nombres_temp);
							
							valor_pagado_temp= c.getColumnIndex("valor_pagado");
							valor_pagado=c.getString(valor_pagado_temp);
														
							TmpIndex = TmpIndex +1;	
							
							total_recaudo=total_recaudo+Integer.parseInt(valor_pagado);
							
							Log.i("Variable", "Datos: "+cedula+ " nombres: "+nombres+ " Valor Pagado: "+valor_pagado+ " Total Rec: "+total_recaudo);
							
							Log.i("Variable", "TmpIndex: "+TmpIndex);
							
							}while(c.moveToNext());
							
							/*String[] Listado_recaudos = new String[TmpIndex];
							
							
							
							ArrayAdapter<String> adaptador =
							new ArrayAdapter<String>(Cobro_Diario.this,
							android.R.layout.simple_list_item_1, Listado_recaudos);
							lv_recaudos.setAdapter(adaptador);*/
							
						
						
						}
						
						db.close();
							
						}
						
						//Alternativa 1: método rawQuery()
						/*Cursor c = db.rawQuery("select c.cedula,c.nombres,sum(r.valor_pagado) as valor_pagado from clientes c,recaudos r " +
								"where c.cedula=r.cedula and r.cobradores_id='"+cobradores_id+"' " +
								"and r.fecha like'%'"+f_recaudo+"'%';", null);*/
			
				
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
