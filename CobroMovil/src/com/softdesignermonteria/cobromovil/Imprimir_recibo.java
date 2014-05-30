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
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class Imprimir_recibo extends Activity {
	
	/** Called when the activity is first created. */

	BluetoothAdapter mBTAdapter;
	BluetoothSocket mBTSocket = null;
	Dialog dialogProgress;
	String BILL, TRANS_ID;
	String PRINTER_MAC_ID;
	final String ERROR_MESSAGE = "There has been an error in printing the bill.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recaudos);
		
		try {
			BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
					.getDefaultAdapter();
			Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

			for (BluetoothDevice device : devices) {

				PRINTER_MAC_ID = device.toString();

			}

			// BILL = getIntent().getStringExtra("TO_PRINT");
			// TRANS_ID = getIntent().getStringExtra("TRANS_ID");

			// PRINTER_MAC_ID = getIntent().getStringExtra("MAC_ID");
			// PRINTER_MAC_ID = "00:1F:B7:02:8F:44";
			// PRINTER_MAC_ID = "10:00:E8:65:46:02";
			// TRANS_ID="12345678";
			BILL = "\nSale Slip No: 12345678" + "          " + "04-08-2011\n";
			BILL = BILL + "1sdsa\n";	
			BILL = BILL + "2asdas\n";
			BILL = BILL + "3asda\n";			
			BILL = BILL + "Total Qty:" + "     " + "2.0\n";
			BILL = BILL + "Total Value:" + "     " + "17625.0\n";

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
