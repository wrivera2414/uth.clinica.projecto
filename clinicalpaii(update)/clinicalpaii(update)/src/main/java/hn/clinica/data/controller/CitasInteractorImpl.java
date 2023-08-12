package hn.clinica.data.controller;

import java.io.IOException;

import hn.clinica.data.entity.CLINICARepositoryImpl;
import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.ResponseCitas;
import hn.clinica.data.entity.ResponsePacientes;
import hn.clinica.views.citas.CitasViewModel;

public class CitasInteractorImpl implements CitasInteractor {
	
	private CLINICARepositoryImpl modelo;
	
	private CitasViewModel vista;
	
	
	public CitasInteractorImpl(CitasViewModel vista) 
	{
		super();
		this.modelo = CLINICARepositoryImpl.getInstance("https://apex.oracle.com/", 89000L);
		this.vista = vista;
	}

	@Override
	public void consultarCitas() {
		try {
			ResponseCitas respuesta = this.modelo.getCita();
			this.vista.refrescarGridCitas(respuesta.getItems());
			
		} catch (IOException e){
			e.printStackTrace();
		} 
		
	}

	@Override
	public void crearNuevaCitas(Citas nuevo) {
		try {
			boolean respuesta = this.modelo.createCita(nuevo);
			this.vista.mostrarMensajeCreacion(respuesta);;
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void actualizarCitas(Citas actualizar) {
		try {
			boolean respuesta = this.modelo.UpdateCita(actualizar);
			this.vista.mostrarMensajeActualizacion(respuesta);;
			
		} catch (IOException e){
			e.printStackTrace();	
	}
}

	@Override
	public void eliminarCitas(String idcita) {
		try {
			boolean respuesta = this.modelo.DeleteCita(idcita);
			this.vista.mostrarMensajeEliminacion(respuesta);
			
		} catch (IOException e){
			e.printStackTrace();
		}
		
	}
	

}