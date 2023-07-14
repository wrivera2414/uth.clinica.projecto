package hn.clinica.data.controller;

import java.io.IOException;

import hn.clinica.data.entity.CLINICARepositoryImpl;
import hn.clinica.data.entity.ResponseCitas;
import hn.clinica.data.entity.ResponsePacientes;
import hn.clinica.views.citas.CitasViewModel;
import hn.clinica.views.pacientes.PacientesViewModel;

public class CitasInteractorImpl implements CitasInteractor {
	
	private CLINICARepositoryImpl modelo;
	
	private CitasViewModel vista;
	
	
	public CitasInteractorImpl(CitasViewModel vista) 
	{
		super();
		this.modelo = CLINICARepositoryImpl.getInstance("https://apex.oracle.com/", 60000L);
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

}
