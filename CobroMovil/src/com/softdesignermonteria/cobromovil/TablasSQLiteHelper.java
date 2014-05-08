package com.softdesignermonteria.cobromovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class TablasSQLiteHelper extends SQLiteOpenHelper {
	 
	/**
	 * Base de datos en desarrollo
	 * version antes de lanzamiento oficial
	 * Version=5
	 */
	
	
		

	/**
     * Creation Tabla Usuarios
     * 
     **/
	String sqlCreateUsuarios   = "CREATE TABLE usuarios ("
									+ "		id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
									+ "		nombre VARCHAR(45) NOT NULL, "
									+ "		clave VARCHAR(45) NOT NULL"
									+ "					)";
    /**
     * Creation Tabla Clientes
     * 
     **/
    String sqlCreateClientes   = "CREATE TABLE clientes ("
									+ "		id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
									+ "		clientes_id INTEGER NOT NULL, "
									+ "		nombres VARCHAR(300) NOT NULL, "
									+ "		direccion VARCHAR(250) NOT NULL, "
									+ "		telefono VARCHAR(20) NOT NULL, "
									+ "		celular VARCHAR(20) NOT NULL"
									+ "					)";
    
    /**
     * Creation Tabla Cobradores
     * 
     **/
    String sqlCreateCobradores = "CREATE TABLE cobradores ("
    							+ "			id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
    							+ "			cobradores_id INTEGER NOT NULL, "
    							+ "			nombres VARCHAR(300) NOT NULL, "
    							+ "			direccion VARCHAR(250) NOT NULL, "
    							+ "			telefono VARCHAR(20) NOT NULL, "
    							+ "			celular VARCHAR(20) NOT NULL"
    							+ "						)";
    
    /**
     * Creation Tabla Cartera
     * 
     **/
    String sqlCreateCartera    = " CREATE TABLE cartera ("
    							+ "			id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
    							+ "			detalle_cxc_id INTEGER NOT NULL, "
    							+ "			creditos_id INTEGER NOT NULL, "
    							+ "			clientes_id INTEGER NOT NULL, "
    							+ "			cedula VARCHAR(20) NOT NULL, "
    							+ "			cobradores_id INTEGER NOT NULL,"
    							+ "			cedula_cobrador VARCHAR(20) NOT NULL, "
    							+ "			vencimiento VARCHAR(10) NOT NULL, "
    							+ "			valor numeric(10,2) NOT NULL "
    							+ "						)";
    
    public TablasSQLiteHelper(Context contexto, String nombre,CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
       
    }
    
   
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * Se ejecuta la sentencia SQL de creaci�n de la tabla
         */
    	creacion_tablas(db);
        
    	String insert_usu = "insert into usuarios (nombre,clave) values ('admin','admin');";
        db.execSQL(insert_usu);
           
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        /**
         * NOTA: Por simplicidad del ejemplo aqu� utilizamos directamente la opci�n de
         * eliminar la tabla anterior y crearla de nuevo vac�a con el nuevo formato.
         * Sin embargo lo normal ser� que haya que migrar datos de la tabla antigua
         * a la nueva, por lo que este m�todo deber�a ser m�s elaborado.
         * Se elimina la versi�n anterior de la tabla
         * 
         */
    	
    	Log.i(this.getClass().toString(), "On Upgrade...");
    	borrar_tablas(db);
        creacion_tablas(db);
        String insert_usu = "insert into usuarios (nombre,clave) values ('admin','admin');";
        db.execSQL(insert_usu);
        Log.i(this.getClass().toString(), "Insercion de usuario por defecto despues de actualizar");
        
    }
    
    private void creacion_tablas(SQLiteDatabase db){
    	/**
    	 * Sentence creation table users
    	 */
    	
        db.execSQL(sqlCreateUsuarios);
        
        /**
    	 * Sentence creation table clients
    	 */
        db.execSQL(sqlCreateClientes);
        
        /**
    	 * Sentence creation table cooperators
    	 */
        db.execSQL(sqlCreateCobradores);
        
        /**
    	 * Sentence creation table carter
    	 */
        db.execSQL(sqlCreateCartera);
    }
    
    
    private void borrar_tablas(SQLiteDatabase db){
    	
    	/**
    	 * Sentence creation table users
    	 */
    	
    	db.execSQL("DROP TABLE IF EXISTS usuarios");
    	/**
    	 * Sentence creation table clients
    	 */
        db.execSQL("DROP TABLE IF EXISTS clientes");
        /**
    	 * Sentence creation table cooperators
    	 */
        db.execSQL("DROP TABLE IF EXISTS cobradores");
        /**
    	 * Sentence creation table carter
    	 */
        db.execSQL("DROP TABLE IF EXISTS cartera");
    }
    
    
    
	
	
}

