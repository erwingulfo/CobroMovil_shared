package com.softdesignermonteria.cobromovil.autocompleteclientes;

import com.softdesignermonteria.cobromovil.Clientes;
import com.softdesignermonteria.cobromovil.R;
import com.softdesignermonteria.cobromovil.clases.ModelClientes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
 
public class CustomAutoCompleteTextChangedListener implements TextWatcher{
 
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
     
    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
    }
     
    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
 
        try{
             
            // if you want to see in the logcat what the user types
            Log.e(TAG, "User input: " + userInput);
     
            Clientes Clientes = ((Clientes) context);
             
            // update the adapater
            Clientes.myAdapter.notifyDataSetChanged();
             
            // get suggestions from the database
            ModelClientes[] myObjs = Clientes.read(userInput.toString());
             
            // update the adapter
            Clientes.myAdapter = new AutocompleteCustomArrayAdapter(Clientes, R.layout.lista_clientes, myObjs);
             
            Clientes.auto.setAdapter(Clientes.myAdapter);
             
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
     
     
 
}