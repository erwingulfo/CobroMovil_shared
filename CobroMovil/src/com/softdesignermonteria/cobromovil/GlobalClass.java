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
	
	private String cobradores_id;
	
	private String cedula_cobrador;
     
	
	
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
	public String getCobradores_id() {
		return cobradores_id;
	}
	public void setCobradores_id(String cobradores_id) {
		this.cobradores_id = cobradores_id;
	}
	public String getCedula_cobrador() {
		return cedula_cobrador;
	}
	public void setCedula_cobrador(String cedula_cobrador) {
		this.cedula_cobrador = cedula_cobrador;
	}
	
 
    
 
}