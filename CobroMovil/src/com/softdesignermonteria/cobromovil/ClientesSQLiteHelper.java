package com.softdesignermonteria.cobromovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class ClientesSQLiteHelper extends SQLiteOpenHelper {
	 
 
	    //Sentencia SQL para crear la tabla de Usuarios
	    
	    String sqlCreateClientes = "CREATE TABLE clientess (id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, nombres VARCHAR(300) NOT NULL, direccion_oficina VARCHAR(250) NOT NULL, direccion_casa VARCHAR(250) NOT NULL, telefono1 VARCHAR(20) NOT NULL)";
	 
	    public ClientesSQLiteHelper(Context contexto, String nombre,
	                               CursorFactory factory, int version) {
	        super(contexto, nombre, factory, version);
	    }
	 
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        //Se ejecuta la sentencia SQL de creaci�n de la tabla
	        db.execSQL(sqlCreateClientes);
	        System.out.println("Base de Datos + Tabla Clientes Creada Primera Vez............");
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
	        //NOTA: Por simplicidad del ejemplo aqu� utilizamos directamente la opci�n de
	        //      eliminar la tabla anterior y crearla de nuevo vac�a con el nuevo formato.
	        //      Sin embargo lo normal ser� que haya que migrar datos de la tabla antigua
	        //      a la nueva, por lo que este m�todo deber�a ser m�s elaborado.
	 
	        //Se elimina la versi�n anterior de la tabla
	        db.execSQL("DROP TABLE IF EXISTS clientes");
	        System.out.println("Base de Datos Borrada............");  
	 
	        //Se crea la nueva versi�n de la tabla
	        db.execSQL(sqlCreateClientes);
	        System.out.println("Base de Datos + Tabla Clientes Creada en Actualizacion............");  
	        
	    }
}


