package com.softdesignermonteria.cobromovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class TablasSQLiteHelper extends SQLiteOpenHelper {
	 
	 
	 //Sentencia SQL para crear la tablas del sistema
    String sqlCreateUsuarios = "CREATE TABLE usuarios (id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(45) NOT NULL, clave VARCHAR(45) NOT NULL)";
    String sqlCreateClientes = "CREATE TABLE clientes (id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, clientes_id INTEGER NOT NULL, nombres VARCHAR(300) NOT NULL, direccion VARCHAR(250) NOT NULL, telefono VARCHAR(20) NOT NULL, celular VARCHAR(25) NOT NULL)";
    
    
    public TablasSQLiteHelper(Context contexto, String nombre,
                               CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
 
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
    	Log.i(this.getClass().toString(), "On Create...");
        db.execSQL(sqlCreateUsuarios);
        db.execSQL(sqlCreateClientes);
        Log.i(this.getClass().toString(), "Base de datos creada..");
        
        String insert_usu = "insert into usuarios (nombre,clave) values ('admin','admin');";
        db.execSQL(insert_usu);
        
        Log.i(this.getClass().toString(), "Usuario creado por defecto..");
           
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
 
        //Se elimina la versión anterior de la tabla
    	
    	Log.i(this.getClass().toString(), "On Upgrade...");
    	
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        
        Log.i(this.getClass().toString(), "Tablas Borradas");  
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreateUsuarios);
        db.execSQL(sqlCreateClientes);
       
        Log.i(this.getClass().toString(), "Tablas Creadas despues de actualizar");  
        
        String insert_usu = "insert into usuarios (nombre,clave) values ('admin','admin');";
        db.execSQL(insert_usu);
        
        Log.i(this.getClass().toString(), "Insercion de usuario por defecto despues de actualizar");
        
    }
}

