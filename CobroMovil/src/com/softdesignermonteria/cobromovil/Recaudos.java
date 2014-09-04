package com.softdesignermonteria.cobromovil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
import android.widget.Toast;

public class Recaudos extends Activity {

	private EditText cedula;
	private Button bt_buscar_cliente;
	private Button bt_recaudar;
	private Button bt_imprimir;
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
	private double valor_recaudado;
	private String clientes_id;
	private String cedula_cliente;
	private String cobradores_id;
	private String creditos_id;
	private String cedula_cobrador;
	private String user_logueado;
	private String valor_cuota_global;
	private String provisional;
	String[] listado = new String[0];

	DecimalFormat formateador = new DecimalFormat("###,###.00");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recaudos);

		/*
		 * Pegar este codigo en todas las actividades que lo requieran
		 */
		final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
		url_servidor = globalVariable.getUrl_servidor();
		nombre_database = globalVariable.getNombre_database();
		version_database = globalVariable.getVersion_database();
		user_logueado = globalVariable.getUserlogueado();

		this.cedula_cobrador = globalVariable.getCedula_cobrador();
		this.cobradores_id = globalVariable.getCobradores_id();

		cedula = (EditText) findViewById(R.id.cedula);
		bt_buscar_cliente = (Button) findViewById(R.id.bt_buscar_cliente);
		bt_recaudar = (Button) findViewById(R.id.bt_recaudar);
		valor_recaudo = (EditText) findViewById(R.id.valor_recaudo);
		
		tv = (TextView) findViewById(R.id.tv);
		tv2 = (TextView) findViewById(R.id.tv2);
		TextView1 = (TextView) findViewById(R.id.TextView1CampoUsuarioVacio);
		TextView2 = (TextView) findViewById(R.id.tv7);
		TextView3 = (TextView) findViewById(R.id.tv3);
		TextView4 = (TextView) findViewById(R.id.tv4);
		tv5 = (TextView) findViewById(R.id.tv5);
		lv = (ListView) findViewById(R.id.lv);

		tv5.setText(user_logueado);
		
		try{
		Bundle bundle = getIntent().getExtras();
		if( !bundle.isEmpty() && bundle != null){
			
			System.out.println("Actividad Recaudo");
			System.out.println("cedula_cliente" + bundle.getString("cedula_cliente"));
			
			cedula.setText(bundle.getString("cedula_cliente"));
			//auto.setEnabled(false);
		}
		}catch(NullPointerException e ){
			Log.e(this.getClass().toString(), "entra a recaudo directamente y bundle es vacio");
		}

		bt_buscar_cliente.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Presiona Boton Buscar");
				if (consultar_clientes()) {
					System.out
							.println("Clientes Consultados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Clientes Consultados Satisfactoriamente");
				} else {
					System.out.println("Oops clientes no encontrado");
					Log.i(this.getClass().toString(),
							"Oops clientes no encontrados");
				}
			}
		});

		bt_recaudar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.i(this.getClass().toString(), "Presiona Boton Recaudar");
				// setContentView(R.layout.activity_menu_clientes);cambios
				// nuevoss
				if (recaudos()) {
					System.out
							.println("Recaudos ingresados Satisfactoriamente");
					Log.i(this.getClass().toString(),
							"Recaudos ingresados Satisfactoriamente");
				} else {
					System.out.println("Oops clientes no encontrado");
					Log.i(this.getClass().toString(),
							"Oops recaudos no ingresados");
				}
			}
		});

		bt_imprimir = (Button) findViewById(R.id.btn_imprimir);
		bt_imprimir.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
		
				/*
				 * Mandamos a la actividad Imprimir y pasamos el codigo
				 * provisional del recibo de caja
				 * */
				Intent i = new Intent();
				i.setClass(Recaudos.this, Imprimir_recibo.class);
				i.putExtra("provisional", provisional);
				
				// i.putExtra("pclave_usuario", clave.getText().toString());
				startActivity(i);
				
				bt_recaudar.setVisibility(View.INVISIBLE);
				
			}
		});
		
		bt_imprimir.setVisibility(View.INVISIBLE);

	}

	public boolean consultar_clientes() {
		boolean sw = true;

		parametro = cedula.getText().toString();

		try {

			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,
					nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			// Si hemos abierto correctamente la base de datos

			if (db != null) {

				tv.setText("");
				tv2.setText("");
				TextView2.setVisibility(View.INVISIBLE);
				TextView3.setVisibility(View.INVISIBLE);
				valor_recaudo.setVisibility(View.INVISIBLE);
				bt_recaudar.setVisibility(View.INVISIBLE);

				Cursor c = db
						.rawQuery(
								"select c.cedula, c.nombres, sum(car.valor) as saldo, car.creditos_id as cod_credito,"
										+ "c.celular,car.detalle_cxc_id,car.clientes_id,car.cobradores_id,car.cedula_cobrador,car.valor as vrcuota "
										+ "from clientes c, cartera car "
										+ "where c.cedula=car.cedula and c.cedula="
										+ "'" + parametro + "'", null);

				String cadena = "select * from clientes where cedula=" + "'"
						+ parametro + "'";

				Log.i("Sql", "Sentencia:" + cadena);

				if (c.moveToFirst()) {

					tv.setVisibility(View.VISIBLE);
					tv2.setVisibility(View.VISIBLE);
					TextView2.setVisibility(View.VISIBLE);
					TextView3.setVisibility(View.VISIBLE);
					valor_recaudo.setVisibility(View.VISIBLE);
					bt_recaudar.setVisibility(View.VISIBLE);

					// Recorremos el cursor hasta que no haya más registros
					do {

						// Log.i("Sql", "pos"+pos+":"+pos);

						String cedula = c.getString(0);
						String nombres = c.getString(1);

						String saldo = c.getString(2);
						String creditos_id = c.getString(3);
						String celular = c.getString(4);
						c.getString(5);
						String clientes_id = c.getString(6);
						c.getString(7);
						c.getString(8);
						String vrcuota = c.getString(9);
						valor_cuota_global = vrcuota;

						this.setCedula_cliente(cedula);
						this.setClientes_id(clientes_id);
						this.setCreditos_id(creditos_id);

						if (cedula == null) {

							tv.setText("");
							tv2.setText("");
							TextView2.setVisibility(View.VISIBLE);
							TextView3.setVisibility(View.VISIBLE);
							valor_recaudo.setVisibility(View.INVISIBLE);
							bt_recaudar.setVisibility(View.INVISIBLE);
							tv.append("Cliente no existe");
							tv2.append("Cliente no tiene crédito");

						} else {

							/*
							 * Formteamos el valor del saldo y la cuota para una
							 * mejor visualización
							 */
							String saldo1 = formateador.format(Double
									.parseDouble(saldo));
							String vr_cuota2 = formateador.format(Double
									.parseDouble(vrcuota));

							tv.append("Cédula: " + cedula + "-Nombres: "
									+ nombres + "-Celular: " + celular + "\n");
							tv2.append("Código Crédito: " + creditos_id
									+ "-Saldo: " + saldo1 + "-Vr Cuota: "
									+ vr_cuota2 + "\n");
							// Log.i("Error",
							// "Saldo formateado:"+formateador.format(saldo1));
							// Log.i("Error",
							// "Vr Cuota formateado:"+formateador.format(vr_cuota2));

						}

					} while (c.moveToNext());

				} else {

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
		boolean sw_print = false;
		parametro = cedula.getText().toString();
		valor_recaudado = Double.parseDouble(valor_recaudo.getText().toString());
		String valor_recaudo_temp = valor_recaudo.getText().toString();
		TextView4.setText("");

		int tmp_cuotas_pagadas = 0;

		if (valor_recaudado == 0) {
			Toast m = Toast.makeText(this,
					"Ingrese un valor diferente de cero.", Toast.LENGTH_SHORT);
			m.show();
			valor_recaudo.setText("");
		}

		if ((valor_recaudo_temp.trim().equals(""))) {
			Toast m = Toast.makeText(this,
					"El campo valor recaudo está vacio.", Toast.LENGTH_SHORT);
			m.show();
			valor_recaudo.requestFocus();
		}

		if (valor_recaudado != 0
				&& valor_recaudado >= Double.parseDouble(valor_cuota_global)) {

			// Log.e("","Valor Recaudado:"+valor_recaudado);

			try {

				TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,
						nombre_database, null, version_database);
				SQLiteDatabase db = usdbh.getWritableDatabase();

				// Si hemos abierto correctamente la base de datos
				if (db != null) {

					String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date());
					/*
					 * Se Genera Codigo provisional
					 */
					provisional = UUID.randomUUID().toString();
					// System.out.println("uuid = " + provisional);
					Log.i("Sql", "temp:" + provisional);

					String insert_recaudos = "insert into recaudos"
							+ " (provisional,clientes_id,cedula,creditos_id,cobradores_id,cedula_cobrador,fecha,valor_pagado,saldo) "
							+ " values ('" + provisional + "','"
							+ this.clientes_id + "','" + this.cedula_cliente
							+ "','" + this.creditos_id + "'," + "'"
							+ this.cobradores_id + "','" + this.cedula_cobrador
							+ "','" + fecha + "','" + valor_recaudado + "','0') ";

					db.execSQL(insert_recaudos);

					Log.e("Sql", "Sentencia:" + insert_recaudos);

					String Sql1 = "select detalle_cxc_id,creditos_id,clientes_id,cedula,"
							+ "cobradores_id,cedula_cobrador,vencimiento,"
							+ "(valor-IFNULL((select sum(rd.valor_pagado) from recaudos_detalles rd where rd.detalle_cxc_id=c.detalle_cxc_id),0)) as valor,"
							+ "(select count(*) from cartera where cedula="
							+ "'"
							+ parametro
							+ "') as tam "
							+ "from cartera c "
							+ "	where c.cedula="
							+ "'"
							+ parametro + "' " + " order by vencimiento asc ";

					Cursor c = db.rawQuery(Sql1, null);
					Log.i("Sql", "Sentencia:" + Sql1);

					double valor_pagado_tmp = valor_recaudado; // 50000

					if (c.moveToFirst()) {
						// Recorremos el cursor hasta que no haya más registros

						String detalle_cxc_id;
						double cuota = 0;
						int tmp = 0;

						do {

							tmp = c.getColumnIndex("detalle_cxc_id");
							detalle_cxc_id = c.getString(tmp);
							tmp = c.getColumnIndex("valor");
							cuota = c.getDouble(tmp);
							Log.i("Sql", "cuota: " + cuota);

							// Log.i("Sql", "pos"+pos+":"+pos);
							String insert_recaudos_detalles = "";

							if (cuota > 0) {

								if (valor_pagado_tmp >= cuota) {
									insert_recaudos_detalles = "insert into recaudos_detalles"
											+ "	(provisional,detalle_cxc_id,valor_pagado) "
											+ " values('"
											+ provisional
											+ "','"
											+ detalle_cxc_id
											+ "','"
											+ cuota
											+ "')";
									Log.e("Sql", "Sentencia:"
											+ insert_recaudos_detalles);

									db.execSQL(insert_recaudos_detalles);
									tmp_cuotas_pagadas = tmp_cuotas_pagadas + 1;
								}

								if (valor_pagado_tmp < cuota) {
									insert_recaudos_detalles = "insert into recaudos_detalles(provisional,detalle_cxc_id,valor_pagado) "
											+ "values('"
											+ provisional
											+ "','"
											+ detalle_cxc_id
											+ "','"
											+ valor_pagado_tmp + "')";
									Log.e("Sql", "Sentencia:"
											+ insert_recaudos_detalles);
									db.execSQL(insert_recaudos_detalles);
									tmp_cuotas_pagadas = tmp_cuotas_pagadas + 1;
								}
								valor_pagado_tmp = valor_pagado_tmp - cuota;

								if (valor_pagado_tmp <= 0) {
									break;
								}// end break;

							}// fin de la condicion si couta>0;

						} while (c.moveToNext());// recorrido de la consulta
													// sql, Cursor c

					}// Ubicarse en el primer registro;
					
					c.close();

					// TablasSQLiteHelper usdbh1 = new
					// TablasSQLiteHelper(this,nombre_database, null,
					// version_database);
					// SQLiteDatabase db1 = usdbh1.getWritableDatabase();

					String Sql2 = " select detalle_cxc_id,valor_pagado "
							+ " from recaudos_detalles " + "	where "
							+ " provisional='" + provisional + "' ";
					Cursor c1 = db.rawQuery(Sql2, null);
					Log.e("", "Yo soy el query :" + Sql2);

					String tmpdetalle_cxc_id;
					double tmpcuota = 0;
					int tmp = 0;
					String[] ListadoList = new String[tmp_cuotas_pagadas];
					int TmpIndex = 0;
					if (c1.moveToFirst()) {

						// Recorremos el cursor hasta que no haya más registros
						do {
							tmp = c1.getColumnIndex("detalle_cxc_id");
							tmpdetalle_cxc_id = c1.getString(tmp);
							tmp = c1.getColumnIndex("valor_pagado");
							tmpcuota = c1.getDouble(tmp);
							Log.i("Sql", "Yo soy TmpIndex: " + TmpIndex);
							/*
							 * "Se pagó la cuota "+detalle_cxc_id+" por valor de: "
							 * +cuota;
							 */
							ListadoList[TmpIndex] = "Se pagó la cuota "
									+ tmpdetalle_cxc_id + " por valor de: "
									+ tmpcuota;
							Log.i("Sql", "array: " + ListadoList[TmpIndex]);
							TmpIndex = TmpIndex + 1;
						} while (c1.moveToNext());

					} // fin ubicarse en primer registro
					c1.close();
					
					
					/*actualizando saldo credito y valor pagado*/
					String sqlsaldo = " select "
									+ " sum((valor-IFNULL((select sum(rd.valor_pagado) from recaudos_detalles rd where rd.detalle_cxc_id=c.detalle_cxc_id),0))) as saldo "
									+ " from cartera c "
									+ "	where c.creditos_id= '" + this.creditos_id + "' ";
					String sqlpagado = " select sum(valor_pagado) "
							+ " from recaudos_detalles " + "	where "
							+ " provisional='" + provisional + "' ";
					
					String sqlUpdateRecaudo = " update recaudos set "
											+ " valor_pagado = IFNULL( ("+sqlpagado+") ,0), "
											+ " saldo        = IFNULL( ("+sqlsaldo+") ,0) "
											+ " where  "
											+ " provisional='" + provisional + "' ";
					db.execSQL(sqlUpdateRecaudo);
					Log.i(this.getClass().toString() + "Recaudos", "Recaudo actualizado " + sqlUpdateRecaudo);
					//Cursor csaldo = db.rawQuery(sqlsaldo, null);
					
					
					
					ArrayAdapter<String> adaptador = new ArrayAdapter<String>(
							Recaudos.this, android.R.layout.simple_list_item_1,
							ListadoList);
					lv.setAdapter(adaptador);

					}

					
					
				    bt_recaudar.setVisibility(View.INVISIBLE);
					bt_imprimir.setVisibility(View.VISIBLE);

				db.close();

			} catch (Exception ex) {
				Log.e("Consulta de clientes", "Error!", ex);
				sw = false;
			}

		} else {

			TextView4.setVisibility(View.VISIBLE);
			TextView4
					.append("Ingrese un valor diferente a cero y/o valor mayor al valor de la cuota");
			Log.e("", "Valor Recaudado:" + valor_recaudado);

		}

		return sw;

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
