package com.softdesignermonteria.cobromovil;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class TablasSQLiteHelper extends SQLiteOpenHelper {

	/**
	 * Base de datos en desarrollo version antes de lanzamiento oficial
	 * Version=10
	 */

	/**
	 * Creation Tabla Usuarios
	 * 
	 **/
	String sqlCreateUsuarios = "CREATE TABLE usuarios ("
			+ "		id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "		nombre VARCHAR(45) NOT NULL, "
			+ "		clave VARCHAR(45) NOT NULL,"
			+ "		cobradores_id VARCHAR(20) NOT NULL,"
			+ "		cedula_cobrador VARCHAR(20) NOT NULL" + "					 )";
	/**
	 * Creation Tabla Clientes
	 * 
	 **/
	String sqlCreateClientes = "CREATE TABLE clientes ("
			+ "		id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "		cedula VARCHAR(20)  NOT NULL, "
			+ "		clientes_id INTEGER NOT NULL, "
			+ "		nombres VARCHAR(300) NOT NULL, "
			+ "		direccion VARCHAR(250) NOT NULL, "
			+ "		telefono VARCHAR(20) NOT NULL, "
			+ "		celular VARCHAR(20) NOT NULL,"
			+ "		referencia_id INTEGER NOT NULL "
			+ "					)";

	/**
	 * Creation Tabla Cobradores
	 * 
	 **/
	String sqlCreateCobradores = "CREATE TABLE cobradores ("
			+ "			id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "			cobradores_id INTEGER NOT NULL, "
			+ "			cedula VARCHAR(20)  NOT NULL, "
			+ "			nombres VARCHAR(300) NOT NULL, "
			+ "			direccion VARCHAR(250) NOT NULL, "
			+ "			telefono VARCHAR(20) NOT NULL, "
			+ "			celular VARCHAR(20) NOT NULL" + "						  )";

	/**
	 * Creation Tabla Cartera
	 * 
	 **/
	String sqlCreateCartera = " CREATE TABLE cartera ("
			+ "			id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "			detalle_cxc_id INTEGER NOT NULL, "
			+ "			creditos_id INTEGER NOT NULL, "
			+ "			clientes_id INTEGER NOT NULL, "
			+ "			cedula VARCHAR(20) NOT NULL, "
			+ "			cobradores_id INTEGER NOT NULL,"
			+ "			cedula_cobrador VARCHAR(20) NOT NULL, "
			+ "			vencimiento VARCHAR(10) NOT NULL, "
			+ "			valor numeric(10,2) NOT NULL " + "						)";
	/**
	 * Creation Tabla Recaudos
	 * 
	 **/

	String sqlCreateRecaudos = " CREATE TABLE recaudos ("
			+ "			id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "			provisional VARCHAR(100) NOT NULL, "
			+ "			clientes_id INTEGER NOT NULL, "
			+ "			cedula VARCHAR(20) NOT NULL, "
			+ "			creditos_id INTEGER NOT NULL, "
			+ "			cobradores_id INTEGER NOT NULL,"
			+ "			cedula_cobrador VARCHAR(20) NOT NULL, "
			+ "			fecha VARCHAR(20) NOT NULL, "
			+ "			valor_pagado numeric(10,2) NOT NULL, "
			+ "			sincronizado VARCHAR(2) default '0' " + "											  )";

	/**
	 * Creation Tabla Recaudos_Detalles
	 * 
	 **/

	String sqlCreateRecaudos_Detalles = " CREATE TABLE recaudos_detalles ("
			+ "			id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
			+ "			provisional VARCHAR(100) NOT NULL, "
			+ "			detalle_cxc_id INTEGER NOT NULL, "
			+ "			valor_pagado numeric(10,2) NOT NULL "
			+ "						                                        )";

	public TablasSQLiteHelper(Context contexto, String nombre,
			CursorFactory factory, int version) {
		super(contexto, nombre, factory, version);

	}

	public void onCreate(SQLiteDatabase db) {
		/**
		 * Se ejecuta la sentencia SQL de creación de la tabla
		 */
		creacion_tablas(db);

		String insert_usu = "insert into usuarios (nombre,clave,cobradores_id,cedula_cobrador) values ('admin','"
				+ md5("admin") + "','2','34444');";
		String cadena = "insert into usuarios (nombre,clave,cobradores_id,cedula_cobrador) values ('admin','admin','2','34444');";
		db.execSQL(insert_usu);
		Log.e("", cadena);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {
		/**
		 * NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la
		 * opción de eliminar la tabla anterior y crearla de nuevo vacía con el
		 * nuevo formato. Sin embargo lo normal será que haya que migrar datos
		 * de la tabla antigua a la nueva, por lo que este método debería ser
		 * más elaborado. Se elimina la versión anterior de la tabla
		 * 
		 */

		Log.i(this.getClass().toString(), "On Upgrade...");
		borrar_tablas(db);
		creacion_tablas(db);
		
		String insert_usu = "insert into usuarios (nombre,clave,cobradores_id,cedula_cobrador) values ('admin','"
				+ md5("admin") + "','2','34444');";
		db.execSQL(insert_usu);
		Log.i(this.getClass().toString(),
				"Insercion de usuario por defecto despues de actualizar");

	}

	private void creacion_tablas(SQLiteDatabase db) {
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
		/**
		 * Sentence creation table Recaudos
		 */
		db.execSQL(sqlCreateRecaudos);
		/**
		 * Sentence creation table Detalle_Recaudos
		 */
		db.execSQL(sqlCreateRecaudos_Detalles);

	}

	private void borrar_tablas(SQLiteDatabase db) {

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

	public static String md5(String s) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes(), 0, s.length());
			String hash = new BigInteger(1, digest.digest()).toString(16);
			return hash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
