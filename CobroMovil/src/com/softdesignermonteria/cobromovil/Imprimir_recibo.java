package com.softdesignermonteria.cobromovil;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class Imprimir_recibo extends Activity {
	
	/** Called when the activity is first created. */
	private String url_servidor;
	private String nombre_database;
	private int version_database;
	private String user_logueado;
	
	BluetoothAdapter mBTAdapter;
	BluetoothSocket mBTSocket = null;
	Dialog dialogProgress;
	String BILL, TRANS_ID;
	String PRINTER_MAC_ID;
	final String ERROR_MESSAGE = "There has been an error in printing the bill.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imprimir_recibo);
		
		try {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

			for (BluetoothDevice device : devices) {

				PRINTER_MAC_ID = device.toString();

			}
			final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
			
			/*
			 * 
			 * asignacion de valores a variables privadas de esta clase
			 * 
			 * */
			url_servidor = globalVariable.getUrl_servidor();
			nombre_database = globalVariable.getNombre_database();
			version_database = globalVariable.getVersion_database();
			user_logueado = globalVariable.getUserlogueado();
			
			// BILL = getIntent().getStringExtra("TO_PRINT");
			// TRANS_ID = getIntent().getStringExtra("TRANS_ID");

			// PRINTER_MAC_ID = getIntent().getStringExtra("MAC_ID");
			// PRINTER_MAC_ID = "00:1F:B7:02:8F:44";
			// PRINTER_MAC_ID = "10:00:E8:65:46:02";
			// TRANS_ID="12345678";
			/*
			BILL =        "\nSale Slip No: 12345678" + "          " + "04-08-2011\n";
			BILL = BILL + "1sdsa\n";	
			BILL = BILL + "2asdas\n";
			BILL = BILL + "3asda\n";			
			BILL = BILL + "Total Qty:" + "     " + "2.0\n";
			BILL = BILL + "Total Value:" + "     " + "17625.0\n";*/
			Bundle bundle = getIntent().getExtras();
			String provisional   = bundle.getString("provisional");
			Log.i("Sql", "temp:" + provisional);
			//String clientes_id   = bundle.getString("clientes_id");
			//String cobradores_id = bundle.getString("cobradores_id");
			
			
			
			BILL =        "\n       INVERSIONES JD            ";
			BILL +=        "\n        316 465 88 59            \n";
			
			BILL = BILL + "\nRecibo No: ;";
			BILL = BILL + "\n"+provisional+"\n";
			
			TablasSQLiteHelper usdbh = new TablasSQLiteHelper(this,
					nombre_database, null, version_database);
			SQLiteDatabase db = usdbh.getWritableDatabase();
			int tmp=0;
			String nombre_cliente="",dir_cliente="",cedula_cliente="",valor_pagado="0",vencimiento="",total="0",nombre_cobrador="",Saldo="0",mvalor_pagado="0",abono="(abono)";
			if (db != null) {
				String sqlEncabezado = "Select  c.nombres as nombre_cliente, c.direccion as dir_cliente, "
												+ "	c.cedula as cedula_cliente, r.valor_pagado, cr.nombres as nombre_cobrador, round(r.saldo) as saldo  "
										+ "from "
										+ "	recaudos r, clientes c, cobradores cr  "
										+ "	where "
										+ " r.clientes_id = c.clientes_id "
										+ " and r.cobradores_id = cr.cobradores_id "
										+ "	and provisional = '"+provisional+"' ";
				Log.i("Sql", "Sentencia:" + sqlEncabezado);
				Cursor recaudos = db.rawQuery(sqlEncabezado,null);
				if (recaudos.moveToFirst()) {
					
					do {
						
						tmp = recaudos.getColumnIndex("nombre_cobrador");
						nombre_cobrador = recaudos.getString(tmp);
						BILL = BILL + "\nCobrador:";
						BILL = BILL + "\n"+nombre_cobrador+"";
						
						tmp = recaudos.getColumnIndex("cedula_cliente");
						cedula_cliente = recaudos.getString(tmp);	
						BILL = BILL + "\nCedula: "+cedula_cliente+"";
						
						tmp = recaudos.getColumnIndex("nombre_cliente");
						nombre_cliente = recaudos.getString(tmp);	
						BILL = BILL + "\nCliente:";
						BILL = BILL + "\n"+nombre_cliente+"";
						
						tmp = recaudos.getColumnIndex("dir_cliente");
						dir_cliente = recaudos.getString(tmp);
						
						BILL = BILL + "\nDireccion:";
						BILL = BILL + "\n"+dir_cliente+"";
						BILL = BILL + "\n";
						BILL = BILL + "\n";
						
						tmp = recaudos.getColumnIndex("saldo");
						Saldo = recaudos.getString(tmp);
						
						BILL = BILL + "\nSaldo del Credito:";
						BILL = BILL + "\n $ "+Saldo+"";
						BILL = BILL + "\n";
						
				
						
						tmp = recaudos.getColumnIndex("valor_pagado");
						total = recaudos.getString(tmp);
						
						
						
					}while (recaudos.moveToNext());
				}
						BILL = BILL + "\nNro: Vencimiento     Valor\n";
						
				String sqlDetalles   = "Select c.vencimiento,d.valor_pagado  from  "
											+ "	 recaudos_detalles d, cartera c "
											+ "  where 1=1 "
											+ "  and c.detalle_cxc_id = d.detalle_cxc_id "
											+ "  and d.provisional = '"+provisional+"' ";
				Log.i("Sql", "Sentencia:" + sqlDetalles);
				Cursor detalles = db.rawQuery(sqlDetalles,null);
				
				  String sqlDetalles2   = "Select max(d.valor_pagado) as mcuota  from  "
						+ "	 recaudos_detalles d, cartera c "
						+ "  where 1=1 "
						+ "  and c.detalle_cxc_id = d.detalle_cxc_id "
						+ "  and d.provisional = '"+provisional+"' ";
					Log.i("Sql", "Sentencia:" + sqlDetalles);
					Cursor detalles2 = db.rawQuery(sqlDetalles2,null);
					if (detalles2.moveToFirst()) {
					
						tmp = detalles2.getColumnIndex("mcuota");
						mvalor_pagado = detalles2.getString(tmp);
						
					}
					detalles2.close();

				int i = 1;
					if (detalles.moveToFirst()) {
						
						do {
							
							tmp = detalles.getColumnIndex("valor_pagado");
							valor_pagado = detalles.getString(tmp);	
							
							tmp = detalles.getColumnIndex("vencimiento");
							vencimiento = detalles.getString(tmp);	
							
							if( Double.parseDouble(valor_pagado) < Double.parseDouble(mvalor_pagado) ){
								BILL = BILL + "\n"+i+" "+vencimiento+" (abono)   "+valor_pagado+"\n";
							}else{
								BILL = BILL + "\n"+i+" "+vencimiento+"           "+valor_pagado+"\n";
							}
							i=i+1;
						}while (detalles.moveToNext());
					}
			}
			
			BILL = BILL + "\nTotal Cancelado:          "+total+"  \n";
			BILL = BILL + "\n";
			BILL = BILL + "\n";
			BILL = BILL + "\n";
			// ¡Gracias por su pago, recuerde pagar puntual y así conserve su crédito!
			BILL = BILL + "     Gracias por su pago \n";
			BILL = BILL + "recuerde pagar puntual y asi conserve su credito\n";
			BILL = BILL + "\n";
			//BILL = BILL + "\n Imprimido desde "+url_servidor+"\n";
			BILL = BILL + "\n   Aplicacion: Cobromovil ";
			BILL = BILL + "\nDisenado Por: DyDsoluciones.net";
			BILL = BILL + "\n      Derechos Reservados    ";
			BILL = BILL + "\n contactanos@dydsoluciones.net";
			BILL = BILL + "\n";
			BILL = BILL + "\n";
			BILL = BILL + "\n";
			BILL = BILL + "\n";
			
			db.close();
			
			mBTAdapter = BluetoothAdapter.getDefaultAdapter();
			dialogProgress = new Dialog(Imprimir_recibo.this);

			try {
				if (mBTAdapter.isDiscovering())
					mBTAdapter.cancelDiscovery();
				else
					mBTAdapter.startDiscovery();
			} catch (Exception e) {
				Log.e("Class ", "My Exe ", e);
			}
			System.out.println("BT Searching status :"
					+ mBTAdapter.isDiscovering());
			if (mBTAdapter == null) {
				Toast.makeText(this, "Device has no bluetooth capability",
						Toast.LENGTH_LONG).show();
				finish();
			} else {
				if (!mBTAdapter.isEnabled()) {
					Intent i = new Intent(
							BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(i, 0);
				}

				// Register the BroadcastReceiver
				IntentFilter filter = new IntentFilter(
						BluetoothDevice.ACTION_FOUND);
				registerReceiver(mReceiver, filter); // Don't forget to
														// unregister during
														// onDestroy

				dialogProgress.setTitle("Finding printer...");
				dialogProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
							public void onDismiss(DialogInterface dialog) {
								dialog.dismiss();
								setResult(RESULT_CANCELED);
								//finish();
							}
						});
				dialogProgress.show();

			}

		} catch (Exception e) {
			Log.e("Class ", "My Exe ", e);
		}
		
		
	}
	
	public void printBillToDevice(final String address) {
		new Thread(new Runnable() {

			public void run() {
				runOnUiThread(new Runnable() {

					public void run() {
						dialogProgress.setTitle("Connecting...");
						dialogProgress.show();
					}

				});

				mBTAdapter.cancelDiscovery();

				try {
					System.out
							.println("**************************#****connecting");
					BluetoothDevice mdevice = mBTAdapter
							.getRemoteDevice(PRINTER_MAC_ID);

					/*
					 * UUID MY_UUID =
					 * UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
					 * mBTSocket =
					 * mdevice.createRfcommSocketToServiceRecord(MY_UUID);
					 */

					Method m = mdevice.getClass().getMethod(
							"createRfcommSocket", new Class[] { int.class });
					mBTSocket = (BluetoothSocket) m.invoke(mdevice,
							Integer.valueOf(1));

					mBTSocket.connect();
					OutputStream os = mBTSocket.getOutputStream();
					// os.flush();
					byte[] buffer = new byte[1024];
					buffer = BILL.getBytes();

					os.write(buffer);
					
					for (int i = 0; i < 10; i++) {
						Thread.currentThread().sleep(1000);
					}

					System.out.println(BILL);
					// os.close();

					mBTSocket.close();
					setResult(RESULT_OK);
					finish();
				} catch (Exception e) {
					Log.e("Class ", "My Exe ", e);
					// Toast.makeText(BluetoothPrint.this, ERROR_MESSAGE,
					// Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					setResult(RESULT_CANCELED);
					finish();

				}

				runOnUiThread(new Runnable() {

					public void run() {
						try {
							dialogProgress.dismiss();
						} catch (Exception e) {
							Log.e("Class ", "My Exe ", e);
						}
					}

				});

			}

		}).start();
	}

	@Override
	protected void onDestroy() {
		Log.i("Dest ", "Checking Ddest");
		super.onDestroy();
		try {
			if (dialogProgress != null)
				dialogProgress.dismiss();
			if (mBTAdapter != null)
				mBTAdapter.cancelDiscovery();
			this.unregisterReceiver(mReceiver);
		} catch (Exception e) {
			Log.e("Class ", "My Exe ", e);
		}
	}

	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {

			try {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					System.out.println("***" + device.getName() + " : "
							+ device.getAddress());

					if (device.getAddress().equalsIgnoreCase(PRINTER_MAC_ID)) {
						mBTAdapter.cancelDiscovery();
						dialogProgress.dismiss();
						Toast.makeText(Imprimir_recibo.this,
								device.getName() + " Printing data",
								Toast.LENGTH_LONG).show();
						printBillToDevice(PRINTER_MAC_ID);
						Toast.makeText(Imprimir_recibo.this,
								device.getName() + " found", Toast.LENGTH_LONG)
								.show();
					}
				}
			} catch (Exception e) {
				Log.e("Class  ", "My Exe ", e);
				Toast.makeText(Imprimir_recibo.this, ERROR_MESSAGE,
						Toast.LENGTH_SHORT).show();

			}
		}
	};

	@Override
	public void onBackPressed() {
		try {
			if (mBTAdapter != null)
				mBTAdapter.cancelDiscovery();
			this.unregisterReceiver(mReceiver);
		} catch (Exception e) {
			Log.e("Class ", "My Exe ", e);
		}
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.imprimir_recibo, menu);
		return true;
	}

}
