package com.softdesignermonteria.cobromovil.autocompleteclientes;

import com.softdesignermonteria.cobromovil.Creditos;
import com.softdesignermonteria.cobromovil.R;
import com.softdesignermonteria.cobromovil.RecaudoMartes;
import com.softdesignermonteria.cobromovil.clases.ModelClientes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
 
public class CustomAutoCompleteRecaudoMartesTextChangedListener implements TextWatcher{
 
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
     
    public CustomAutoCompleteRecaudoMartesTextChangedListener(Context context){
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
     
            RecaudoMartes RecaudoMartes = ((RecaudoMartes) context);
             
            // update the adapater
            RecaudoMartes.myAdapter.notifyDataSetChanged();
             
            // get suggestions from the database
            ModelClientes[] myObjs = RecaudoMartes.Read(userInput.toString());
             
            // update the adapter
            RecaudoMartes.myAdapter = new AutocompleteCustomArrayAdapter(RecaudoMartes, R.layout.lista_clientes, myObjs);
             
            RecaudoMartes.auto.setAdapter(RecaudoMartes.myAdapter);
             
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
     
     
 
}