package com.softdesignermonteria.cobromovil.autocompleteclientes;

import com.softdesignermonteria.cobromovil.Clientes;
import com.softdesignermonteria.cobromovil.MainActivity;
import com.softdesignermonteria.cobromovil.R;
import com.softdesignermonteria.cobromovil.clases.ModelClientes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AutocompleteCustomArrayAdapter extends ArrayAdapter<ModelClientes> {

	final String TAG = "AutocompleteCustomArrayAdapter.java";

	Context mContext;
	int layoutResourceId;
	ModelClientes data[] = null;

	public AutocompleteCustomArrayAdapter(Context mContext,
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
				LayoutInflater inflater = ((Clientes) mContext)
						.getLayoutInflater();
				convertView = inflater.inflate(layoutResourceId, parent, false);
			}

			// object item based on the position
			ModelClientes objectItem = data[position];

			// get the TextView and then set the text (item name) and tag (item
			// ID) values
			TextView textViewItemSubTitulo = (TextView) convertView
					.findViewById(R.id.LblSubTitulo);
			textViewItemSubTitulo.setText(objectItem.getNombre());

			TextView textViewItemTitulo = (TextView) convertView
					.findViewById(R.id.LblTitulo);
			textViewItemTitulo.setText(objectItem.getClientes_id());
			
			
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