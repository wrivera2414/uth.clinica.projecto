package hn.clinica.data.controller;

import java.io.IOException;

import hn.clinica.data.entity.CLINICARepositoryImpl;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.data.entity.ResponseCitas;
import hn.clinica.data.entity.ResponseMedicamento;
import hn.clinica.data.entity.ResponsePacientes;
import hn.clinica.views.citas.CitasViewModel;
import hn.clinica.views.consulta.ConsultaViewModel;
import hn.clinica.views.medicamentos.MedicamentosViewModel;
import hn.clinica.views.pacientes.PacientesViewModel;

public class ConsultaInteractorImpl implements PacientesInteractor{
	

	private CLINICARepositoryImpl modelo;
	
	private ConsultaViewModel vista;
	
	
	
	public ConsultaInteractorImpl(ConsultaViewModel vista) 
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

	}

}
