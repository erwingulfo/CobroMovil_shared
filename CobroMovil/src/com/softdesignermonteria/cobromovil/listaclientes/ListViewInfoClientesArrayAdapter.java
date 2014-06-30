package com.softdesignermonteria.cobromovil.listaclientes;

import com.softdesignermonteria.cobromovil.R;
import com.softdesignermonteria.cobromovil.clases.ModelClientes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListViewInfoClientesArrayAdapter extends ArrayAdapter<ModelClientes> {

	final String TAG = "AutocompleteCustomArrayAdapter.java";

	Context mContext;
	int layoutResourceId;
	ModelClientes data[] = null;

	public ListViewInfoClientesArrayAdapter(Context mContext,
			int layoutResourceId, ModelClientes[] data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {

			/*
			 * The convertView argument is essentially a "ScrapView" as
			 * described is Lucas post
			 * http://lucasr.org/2012/04/05/performance-tips
			 * -for-androids-listview/ It will have a non-null value when
			 * ListView is asking you recycle the row layout. So, when
			 * convertView is not null, you should simply update its contents
			 * instead of inflating a new row layout.
			 */
			if (convertView == null) {
				// inflate the layout
				LayoutInflater inflater = ((Activity) mContext) 
						.getLayoutInflater();
				convertView = inflater.inflate(layoutResourceId, parent, false);
			}

			// object item based on the position
			ModelClientes objectItem = data[position];

			// get the TextView and then set the text (item name) and tag (item
			// ID) values
			TextView txt1 = (TextView) convertView
					.findViewById(R.id.LbInfoClientesClientes_id);
			txt1.setText(objectItem.getClientes_id());

			TextView txt2 = (TextView) convertView
					.findViewById(R.id.LbInfoClientesCedula);
			txt2.setText(objectItem.getCedula());
			
			TextView txt3 = (TextView) convertView
					.findViewById(R.id.LbInfoClientesDireccion);
			txt3.setText(objectItem.getDireccion());
			
			TextView txt4 = (TextView) convertView
					.findViewById(R.id.LbInfoClientesTelefonos);
			txt4.setText(objectItem.getTelefono());
			
			TextView txt5 = (TextView) convertView
					.findViewById(R.id.LbInfoClientesCelular);
			txt5.setText(objectItem.getCelular());
			
			TextView txt6 = (TextView) convertView
					.findViewById(R.id.LbInfoClientesNombres);
			txt6.setText(objectItem.getNombre());
			
		
			
			// in case you want to add some style, you can do something like:
			//textViewItemTitulo.setBackgroundColor(Color.CYAN);

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;

	}
}