package com.softdesignermonteria.cobromovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class UsuariosSQLiteHelper extends SQLiteOpenHelper {
	 
 
	    //Sentencia SQL para crear la tabla de Usuarios
	    String sqlCreate = "CREATE TABLE usuarios (id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(45) NOT NULL, clave VARCHAR(45) NOT NULL)";
	 
	    public UsuariosSQLiteHelper(Context contexto, String nombre,
	                               CursorFactory factory, int version) {
	        super(contexto, nombre, factory, version);
	    }
	 
	 
	    @Override
	    public void onCreate(SQLiteDatabase db) {
	        //Se ejecuta la sentencia SQL de creación de la tabla
	        db.execSQL(sqlCreate);
	        System.out.println("Base de Datos Creada............");
	        
	        
	       /* String insert_usu = "insert into usuarios (nombre,clave) values ('admin','admin');";
	        db.execSQL(insert_usu);
	        
	        System.out.println("Insertando Registro por defecto............");*/
	        
	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
	        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
	        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
	        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
	        //      a la nueva, por lo que este método debería ser más elaborado.
	 
	        //Se elimina la versión anterior de la tabla
	        db.execSQL("DROP TABLE IF EXISTS usuarios");
	        System.out.println("Base de Datos Borrada............");  
	 
	        //Se crea la nueva versión de la tabla
	        db.execSQL(sqlCreate);
	        System.out.println("Base de Datos Creada en Actualizacion............");  
	        
	        String insert_usu = "insert into usuarios (nombre,clave) values ('admin','admin');";
	        db.execSQL(insert_usu);
	        
	        System.out.println("Insertando Registro por defecto............");
	        
	    }
}


