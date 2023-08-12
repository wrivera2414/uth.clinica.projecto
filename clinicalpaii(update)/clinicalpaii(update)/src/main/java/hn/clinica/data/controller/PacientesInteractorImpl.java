package hn.clinica.data.controller;

import java.io.IOException;

import hn.clinica.data.entity.CLINICARepositoryImpl;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.data.entity.ResponsePacientes;
import hn.clinica.views.pacientes.PacientesViewModel;

public class PacientesInteractorImpl implements PacientesInteractor {
	
	private CLINICARepositoryImpl modelo;
	
	private PacientesViewModel vista;
	
	
	public PacientesInteractorImpl(PacientesViewModel vista) 
	{
		super();
		this.modelo = CLINICARepositoryImpl.getInstance("https://apex.oracle.com/", 60000L);
		this.vista = vista;
	}


	@Override
	public void consultarPacientes() {
		try {
			ResponsePacientes respuesta = this.modelo.getPacientes();
			this.vista.refrescarGridPacientes(respuesta.getItems());
			
		} catch (IOException e){
			e.printStackTrace();
		} 
	}


	@Override
	public void crearPacientes(Pacientes nuevo) {
		try {
			boolean respuesta = this.modelo.createPaciente(nuevo);
			this.vista.mostrarMensajeCreacion(respuesta);
			
		} catch (IOException e){
			e.printStackTrace();
		} 		
	}


	@Override
	public void modificarPacientes(Pacientes actualizar) {
		try {
			boolean respuesta = this.modelo.updatePacientes(actualizar);
			this.vista.mostrarMensajeActualizacionPacientes(respuesta);
			
		} catch (IOException e){
			e.printStackTrace();	
	}		
	}


	@Override
	public void eliminarPaciente(String identidad) {
		try {
			boolean respuesta = this.modelo.deletePacientes(identidad);
			this.vista.mostrarMensajeEliminacionPacientes(respuesta);
			
		} catch (IOException e){
			e.printStackTrace();	
	}			
	}


}
