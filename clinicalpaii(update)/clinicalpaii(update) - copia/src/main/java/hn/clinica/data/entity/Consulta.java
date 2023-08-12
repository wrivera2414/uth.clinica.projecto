package hn.clinica.data.entity;

public class Consulta {
	
	 private String identidad;
     private String nombre;
     private String telefono;
     private String medicamento;
     private String stocks;
     private String fecha;
     
     

	public String getMedicamento() {
		return medicamento;
	}
	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
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
	public String getStocks() {
		return stocks;
	}
	public void setStocks(String stocks) {
		this.stocks = stocks;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
     

}
