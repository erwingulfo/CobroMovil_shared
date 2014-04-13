package com.softdesignermonteria.cobromovil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Menu_clientes extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_clientes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_clientes, menu);
		return true;
	}

}
