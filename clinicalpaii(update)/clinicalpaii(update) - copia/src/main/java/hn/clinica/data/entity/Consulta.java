package hn.clinica.data.entity;

public class Consulta extends Pacientes {
	
	 private String identidad;
     private String nombre;
     private String telefono;
     private String principioa;
     private String stock;
     private String fecha;

     
	public String getPrincipioa() {
		return principioa;
	}
	public void setPrincipioa(String principioa) {
		this.principioa = principioa;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getIdentidad() {
		return identidad;
	}
	public void setIdentidad(String identidad) {
		this.identidad = identidad;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	

}
