package com.softdesignermonteria.cobromovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TablasSQLiteHelper extends SQLiteOpenHelper {
	 
	 
	 //Sentencia SQL para crear la tablas del sistema
    String sqlCreateUsuarios = "CREATE TABLE usuarios (id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(45) NOT NULL, clave VARCHAR(45) NOT NULL)";
    String sqlCreateClientes = "CREATE TABLE clientes (id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, nombres VARCHAR(300) NOT NULL, direccion_oficina VARCHAR(250) NOT NULL, direccion_casa VARCHAR(250) NOT NULL, telefono1 VARCHAR(20) NOT NULL)";
    
    
    public TablasSQLiteHelper(Context contexto, String nombre,
                               CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
 
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreateUsuarios);
        db.execSQL(sqlCreateClientes);
        System.out.println("Base de Datos Creada............");
           
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
 
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS clientes");
        
        System.out.println("Base de Datos Borrada............");  
 
        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreateUsuarios);
        db.execSQL(sqlCreateClientes);
       
        System.out.println("Base de Datos Creada en Actualizacion............");  
        
        String insert_usu = "insert into usuarios (nombre,clave) values ('admin','admin');";
        db.execSQL(insert_usu);
        
        System.out.println("Insertando Registro por defecto............");
        
    }
}

