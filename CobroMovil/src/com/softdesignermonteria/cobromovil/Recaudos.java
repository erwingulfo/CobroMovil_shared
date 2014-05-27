package com.softdesignermonteria.cobromovil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.w3c.dom.Text;

import android.R.integer;
import android.R.string;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Recaudos extends Activity {
	
	private EditText cedula;
	private Button bt_buscar_cliente;
	private Button bt_recaudar;
	private TextView tv;
	private TextView tv2;
	private TextView TextView1;
	private TextView TextView2;
	private TextView TextView3;
	private TextView TextView4;
	private TextView tv5;
	private EditText valor_recaudo;
	private ListView lv;
	
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	private String parametro;
	private int valor_recaudado;
	private String clientes_id;
	private String cedula_cliente;
	private String cobradores_id;
	private String creditos_id;
	private String cedula_cobrador;
	private String user_logueado;
	private String valor_cuota_global;
	private int tam=0;
		
	String[] listado = new String[0];
	
	

	DecimalFormat formateador = new DecimalFormat("###,###.00");


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recaudos);
			
		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 * */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor     = globalVariable.getUrl_servidor();
		nombre_database  = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		user_logueado = globalVariable.getUserlogueado();
					
		this.cedula_cobrador = globalVariable.getCedula_cobrador();
		this.cobradores_id = globalVariable.getCobradores_id();
		
		cedula = (EditText)findViewById(R.id.cedula);
		bt_buscar_cliente = (Button)findViewById(R.id.bt_buscar_cliente);
		bt_recaudar = (Button)findViewById(R.id.bt_recaudar);
		valor_recaudo = (EditText)findViewById(R.id.valor_recaudo);
		tv = (TextView)findViewById(R.id.tv);
		tv2 = (TextView)findViewById(R.id.tv2);
		TextView1 = (TextView)findViewById(R.id.tv1);
		TextView2 = (TextView)findViewById(R.id.tv7);
		TextView3 = (TextView)findViewById(R.id.tv3);
		TextView4 = (TextView)findViewById(R.id.tv4);
		tv5 = (TextView)findViewById(R.id.tv5);
		lv = (ListView)findViewById(R.id.lv);
						
		tv5.setText(user_logueado);
			
		bt_buscar_cliente.setOnClickListener(new OnClickListener() {
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
		
		bt_recaudar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Presiona Boton Recaudar");
				// setContentView(R.layout.activity_menu_clientes);cambios nuevoss
				if (recaudos()) {
					System.out
							.println("Recaudos ingresados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Recaudos ingresados Satisfactoriamente");
				} else {
					System.out.println("Oops clientes no encontrado");
					Log.i(this.getClass().toString(), "Oops recaudos no ingresados");
				}
			}
		});
		
	}
	
	public boolean consultar_clientes() {
		boolean sw = true;
		
		parametro=cedula.getText().toString();
			
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
								
			if (db != null) {
				
					tv.setText("");
					tv2.setText("");
					TextView2.setVisibility(View.INVISIBLE);
					TextView3.setVisibility(View.INVISIBLE);
					valor_recaudo.setVisibility(View.INVISIBLE);
					bt_recaudar.setVisibility(View.INVISIBLE);
				
					Cursor c = db.rawQuery("select c.cedula, c.nombres, sum(car.valor) as saldo, car.creditos_id as cod_credito," +
					"c.celular,car.detalle_cxc_id,car.clientes_id,car.cobradores_id,car.cedula_cobrador,car.valor as vrcuota " +
					"from clientes c, cartera car " + "where c.cedula=car.cedula and c.cedula="+"'"+parametro+"'", null);
					
					String cadena="select * from clientes where cedula="+"'"+parametro+"'";
					
					Log.i("Sql", "Sentencia:"+cadena);
															
					if (c.moveToFirst()) {
						
						tv.setVisibility(View.VISIBLE);
						tv2.setVisibility(View.VISIBLE);
						TextView2.setVisibility(View.VISIBLE);
						TextView3.setVisibility(View.VISIBLE);
					    valor_recaudo.setVisibility(View.VISIBLE);
					    bt_recaudar.setVisibility(View.VISIBLE);
					    
					     //Recorremos el cursor hasta que no haya más registros
						do{
					
						//Log.i("Sql", "pos"+pos+":"+pos);
							
						String cedula = c.getString(0);
					    String nombres = c.getString(1);
					    
						String saldo = c.getString(2);
					    String creditos_id = c.getString(3);
					    String celular = c.getString(4);
					    String detalle_cxc_id=c.getString(5);
					    String clientes_id=c.getString(6);
					    String cobradores_id=c.getString(7);
					    String cedula_cobrador=c.getString(8);
					    String vrcuota=c.getString(9);
					    valor_cuota_global=vrcuota;
					    
					    this.setCedula_cliente(cedula);
					    this.setClientes_id(clientes_id);
					    this.setCreditos_id(creditos_id);
					    
					    if(cedula==null){
					    	
					       tv.setText("");
					       tv2.setText("");
					       TextView2.setVisibility(View.VISIBLE);
					       TextView3.setVisibility(View.VISIBLE);
					       valor_recaudo.setVisibility(View.INVISIBLE);
						   bt_recaudar.setVisibility(View.INVISIBLE);
					       tv.append("Cliente no existe");
					       tv2.append("Cliente no tiene crédito");
					   					    
					    }else{
					    	
					    	/*Formteamos el valor del saldo y la cuota para una mejor visualización*/
					    	String saldo1=formateador.format(Double.parseDouble(saldo));
					    	String vr_cuota2=formateador.format(Double.parseDouble(vrcuota));
					    	
					    	tv.append("Cédula: "+ cedula +"-Nombres: " + nombres + "-Celular: "+ celular +"\n");
					    	tv2.append("Código Crédito: "+ creditos_id +"-Saldo: "+ saldo1 +"-Vr Cuota: "+ vr_cuota2 +"\n");
					    	//Log.i("Error", "Saldo formateado:"+formateador.format(saldo1));
					    	//Log.i("Error", "Vr Cuota formateado:"+formateador.format(vr_cuota2));
					    					    						  
					    }
					    
					   					    					    
					    } while(c.moveToNext());
										
				    }else{

				    	 tv.setText("");
					     tv2.setText("");
					     TextView2.setVisibility(View.VISIBLE);
					     TextView3.setVisibility(View.VISIBLE);
					     valor_recaudo.setVisibility(View.INVISIBLE);
						 bt_recaudar.setVisibility(View.INVISIBLE);
					     tv.append("Cliente no existe");
					     tv2.append("Cliente no tiene crédito");
				    }
				  
				 
				}
				
		  db.close();

		} catch (Exception ex) {
			Log.e("Consulta de clientes", "Error!", ex);
			sw = false;
		}

		return sw;
		

	}

	public boolean recaudos() {
		
		boolean sw = true;
		
		parametro=cedula.getText().toString();
		valor_recaudado=Integer.parseInt(valor_recaudo.getText().toString());
		String valor_recaudo_temp=valor_recaudo.getText().toString();
		TextView4.setText("");
		
		if(valor_recaudado==0){
			
			 Toast m=Toast.makeText(this, "Ingrese un valor diferente de cero.", Toast.LENGTH_SHORT);
			 m.show();
			 valor_recaudo.setText("");
			
		}
		
		if((valor_recaudo_temp.trim().equals(""))){
			
			 Toast m=Toast.makeText(this, "El campo valor recaudo está vacio.", Toast.LENGTH_SHORT);
			 m.show();
			 valor_recaudo.requestFocus();
			
		}
		
		if(valor_recaudado!=0 && valor_recaudado>=Integer.parseInt(valor_cuota_global)){
							
		//Log.e("","Valor Recaudado:"+valor_recaudado);
		
		try {
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos
			
								
			if (db != null) {
					
				
					String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					
					String provisional = UUID.randomUUID().toString();
			    	//System.out.println("uuid = " + provisional);
			    	Log.i("Sql", "temp:"+provisional);
					
					String insert_recaudos="insert into recaudos(provisional,clientes_id,cedula,creditos_id,cobradores_id," +
						       "cedula_cobrador,fecha,valor_pagado)" +
						       "values('"+provisional+"','"+this.clientes_id+"','"+this.cedula_cliente+"','"+this.creditos_id+"'," +
						       "'"+this.cobradores_id+"','"+this.cedula_cobrador+"','"+fecha+"','"+valor_recaudado+"')";	
						       
						       String sql="insert into recaudos(provisional,clientes_id,cedula,creditos_id,cobradores_id," +
						       "cedula_cobrador,fecha,valor_pagado)" +
						       "values('"+provisional+"','"+this.clientes_id+"','"+this.cedula_cliente+"','"+this.creditos_id+"'," +
						       "'"+cobradores_id+"','"+this.cedula_cobrador+"','"+fecha+"','"+valor_recaudado+"'";
						       db.execSQL(insert_recaudos);
						       
						       Log.e("Sql", "Sentencia:"+sql);
						       
			       Cursor c = db.rawQuery("select detalle_cxc_id,creditos_id,clientes_id,cedula," +
							"cobradores_id,cedula_cobrador,vencimiento," +
							"(valor-IFNULL((select sum(rd.valor_pagado) from recaudos_detalles rd where rd.detalle_cxc_id=c.detalle_cxc_id),0)) as valor," +
							"(select count(*) from cartera where cedula="+"'"+parametro+"') as tam " +
							"from cartera c where c.cedula="+"'"+parametro+"' order by vencimiento asc", null);
					
					String cadena="select detalle_cxc_id,creditos_id,clientes_id,cedula," +
							"cobradores_id,cedula_cobrador,vencimiento," +
							"(valor-IFNULL((select sum(rd.valor_pagado) from recaudos_detalles rd " +
							"where rd.detalle_cxc_id=c.detalle_cxc_id),0)) as valor," +
							"from cartera c where c.cedula="+"'"+parametro+"' order by vencimiento asc";
					
					Log.i("Sql", "Sentencia:"+cadena);	     
					
						       
					int valor_pagado_tmp = valor_recaudado;     //50000 
															
					if (c.moveToFirst()) {
					
					//Recorremos el cursor hasta que no haya más registros
						
					String detalle_cxc_id;
					int cuota = 0;
					int tmp = 0;
								
										
					do{
						
						tmp = c.getColumnIndex("detalle_cxc_id");
						detalle_cxc_id=c.getString(tmp);
						tmp = c.getColumnIndex("valor");
						
						cuota = c.getInt(tmp);
						Log.i("Sql", "cuota: "+cuota);
						
						//Log.i("Sql", "pos"+pos+":"+pos);
						String insert_recaudos_detalles="";
						
						if(cuota>0){
																   						   
							
							if(valor_pagado_tmp>=cuota){ 
					    	
								
								insert_recaudos_detalles="insert into recaudos_detalles(provisional,detalle_cxc_id,valor_pagado) " +
					       		"values('"+provisional+"','"+detalle_cxc_id+"','"+cuota+"')";
								db.execSQL(insert_recaudos_detalles);
					       
						        String cuota1=formateador.format(cuota);
						     
						        /*TextView4.setVisibility(View.VISIBLE);
						        TextView4.append("Se pagó la cuota Nro: "+ detalle_cxc_id +"-por valor de: " + cuota1 +"\n");*/
						       
						       						       
						      /* ArrayAdapter<String> adaptador =
							   new ArrayAdapter<String>(Recaudos.this,
							   android.R.layout.simple_list_item_1, menu);
							   lv.setAdapter(adaptador);*/
						       
						       String sql2="insert into recaudos_detalles(provisional,detalle_cxc_id,valor_pagado) " +
						       		"values('"+provisional+"','"+detalle_cxc_id+"','"+cuota1+"'";
						       
						       Log.e("Sql", "Sentencia:"+sql2);
					       					   					    
							}
							
						    if(valor_pagado_tmp<cuota){
						    
						    			    	   
							       insert_recaudos_detalles="insert into recaudos_detalles(provisional,detalle_cxc_id,valor_pagado) " +
							       		"values('"+provisional+"','"+detalle_cxc_id+"','"+valor_pagado_tmp+"')";
							       db.execSQL(insert_recaudos_detalles);
							       
							       String cuota2=formateador.format(valor_pagado_tmp);
							       
							       /*TextView4.setVisibility(View.VISIBLE);
							       TextView4.append("Se abonó a la cuota Nro: "+ detalle_cxc_id +"-por valor de: " + cuota2 +"\n");
							       Log.e("Sql", "Sentencia 2:"+insert_recaudos_detalles);*/
							       
							       
							       //Log.i("Sql", "tam1 array: "+tam1);
							      /* menu[]= "" + detalle_cxc_id + "-" + cuota2 + "-";
							       
							       ArrayAdapter<String> adaptador =
								   new ArrayAdapter<String>(Recaudos.this,
								   android.R.layout.simple_list_item_1, menu);
								   lv.setAdapter(adaptador);*/
							     
							    }
						
					        
						    	valor_pagado_tmp=valor_pagado_tmp-cuota;
					    
							    if(valor_pagado_tmp<=0){
							    	
							    	break;
							    	
							    }//end break;
							   
							   
						    }//fin de la condicion si couta>0;
					    
					 					    
					    } while(c.moveToNext());//recorrido de la consulta sql, Cursor c
										
				    }//Ubicarse en el primer registro;
					
					TablasSQLiteHelper usdbh1 = new TablasSQLiteHelper(this,nombre_database, null, version_database);
					SQLiteDatabase db1 = usdbh1.getWritableDatabase();
					
								
					Cursor c1 = db1.rawQuery("select detalle_cxc_id,valor_pagado," +
							"(select count(*) from recaudos_detalles rd where rd.provisional=rd1.provisional) as tam " +
							"from recaudos_detalles rd1 where rd1.provisional='"+provisional+"'",null);
																	
					String sql2="select detalle_cxc_id,valor_pagado," +
							"(select count(*) from recaudos_detalles rd where rd.provisional=rd1.provisional) as tam " +
							"from recaudos_detalles rd1 where rd1.provisional='"+provisional+"'";
				    
					Log.e("","Yo soy el query :"+sql2);
				    
				    String detalle_cxc_id;
					int cuota = 0;
					int tmp = 0;
					int tam1=0;
					int k=0;
								    
					if (c1.moveToFirst()) {
				
					//Recorremos el cursor hasta que no haya más registros
					do{
						
						tam1 = c1.getColumnIndex("tam");
					    tam = c1.getInt(tam1);
					    
					    tmp = c1.getColumnIndex("detalle_cxc_id");
						detalle_cxc_id=c1.getString(tmp);
						
						tmp = c1.getColumnIndex("valor_pagado");
						cuota = c1.getInt(tmp);		
												
						Log.i("Sql", "Yo soy k: "+k);
						/*"Se pagó la cuota "+detalle_cxc_id+" por valor de: "+cuota;*/
						listado[k]= "Se pagó la cuota "+detalle_cxc_id+" por valor de: "+cuota;
						Log.i("Sql", "array: "+listado[k]);
						
						
						k=k+1;
				 
									
					}while(c1.moveToNext());
					
					
					}
					
				
			   }
			
			   db.close();
	
				} catch (Exception ex) {
					Log.e("Consulta de clientes", "Error!", ex);
					sw = false;
			 }
		
		  }else{
			  
			  TextView4.setVisibility(View.VISIBLE);
			  TextView4.append("Ingrese un valor diferente a cero y/o valor mayor al valor de la cuota");
			  Log.e("","Valor Recaudado:"+valor_recaudado);
			  
		  }
	   
		return sw;
	   

	}
	

	private void setListAdapter(ArrayAdapter<String> arrayAdapter) {
		// TODO Auto-generated method stub
		
	}

	private int Double(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int Integer(String string) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recaudos, menu);
		return true;
	}


	public void setClientes_id(String clientes_id) {
		this.clientes_id = clientes_id;
	}

	public void setCedula_cliente(String cedula_cliente) {
		this.cedula_cliente = cedula_cliente;
	}

	public String getCreditos_id() {
		return creditos_id;
	}

	public void setCreditos_id(String creditos_id) {
		this.creditos_id = creditos_id;
	}

}
