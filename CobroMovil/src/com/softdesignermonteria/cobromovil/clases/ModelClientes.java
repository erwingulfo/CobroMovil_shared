package com.softdesignermonteria.cobromovil.clases;

public class ModelClientes {
		private String clientes_id;
	    private String nombre;
	    private String direccion;
	    private String telefono;
	    private String cedula;
	    private String celular;
	    private String valor_recaudado;
	    
	    
	    
	    public ModelClientes(String clientes_id, String nombre,
				String direccion, String telefono, String cedula, String celular,String valor_recaudado) {
			super();
			this.clientes_id = clientes_id;
			this.nombre = nombre;
			this.direccion = direccion;
			this.telefono = telefono;
			this.cedula = cedula;
			this.celular = celular;
			this.valor_recaudado = valor_recaudado;
			
		}
	    
	    
		public String getClientes_id() {
			return clientes_id;
		}
		public void setClientes_id(String clientes_id) {
			this.clientes_id = clientes_id;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getDireccion() {
			return direccion;
		}
		public void setDireccion(String direccion) {
			this.direccion = direccion;
		}
		public String getTelefono() {
			return telefono;
		}
		public void setTelefono(String telefono) {
			this.telefono = telefono;
		}


		public String getCedula() {
			return cedula;
		}


		public void setCedula(String cedula) {
			this.cedula = cedula;
		}


		public String getCelular() {
			return celular;
		}


		public void setCelular(String celular) {
			this.celular = celular;
		}


		public String getValor_recaudado() {
			return valor_recaudado;
		}


		public void setValor_recaudado(String valor_recaudado) {
			this.valor_recaudado = valor_recaudado;
		}
		
}


