package com.softdesignermonteria.cobromovil;

import android.app.Application;

public class GlobalClass extends Application{
     
	/**
	 * Variable Global Version Database
	 * 
	 * */
	private int version_database;
	
	private String nombre_database;
	
	private String url_servidor;
     
	
	
	public int getVersion_database() {
		return version_database;
	}
	public void setVersion_database(int version_database) {
		this.version_database = version_database;
	}
	public String getNombre_database() {
		return nombre_database;
	}
	public void setNombre_database(String nombre_database) {
		this.nombre_database = nombre_database;
	}
	public String getUrl_servidor() {
		return url_servidor;
	}
	public void setUrl_servidor(String url_servidor) {
		this.url_servidor = url_servidor;
	}
	
 
    
 
}