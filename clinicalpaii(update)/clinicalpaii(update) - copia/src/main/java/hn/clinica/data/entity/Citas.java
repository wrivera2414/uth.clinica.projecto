package hn.clinica.data.entity;

public class Citas {

    private String idcita;
    private String fecha;
    private String identidad;
    private String paciente;
    private String direccion;
    private String telefono;
    private String detalle;    

    public String getIdcita() {
        return idcita;
    }
    public void setIdcita(String idcita) {
        this.idcita = idcita;
    }
    public String getFecha() {
        return fecha; 
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public String getPaciente() {
        return paciente;
    }
    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
  
	public String getDetalle() {
		return detalle;
	}
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getIdentidad() {
		return identidad;
	}
	public void setIdentidad(String identidad) {
		this.identidad = identidad;
	}

}
