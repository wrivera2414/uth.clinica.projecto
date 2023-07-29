package hn.clinica.data.entity;

public class Medicamentos{

    private String codigo;
    private String nombre	;
    private String fabricante;
    private String principioa;
    private String fechav;
    private String stock;
    	
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrincipioa() {
		return principioa;
	}
	public void setPrincipioa(String principioa) {
		this.principioa = principioa;
	}
	public String getFechav() {
		return fechav;
	}
	public void setFechav(String fechav) {
		this.fechav = fechav;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getFabricante() {
		return fabricante;
	}
	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}
    

   
}
