package hn.clinica.data.controller;

import java.io.IOException;

import hn.clinica.data.entity.CLINICARepositoryImpl;
import hn.clinica.data.entity.ResponseConsulta;
import hn.clinica.views.consulta.ConsultaViewModel;

public class ConsultaInteractorImpl implements ConsultaInteractor {
	
private CLINICARepositoryImpl modelo;
	
	private ConsultaViewModel vista;
	
	
	public ConsultaInteractorImpl(ConsultaViewModel vista) 
	{
		super();
		this.modelo = CLINICARepositoryImpl.getInstance("https://apex.oracle.com/", 60000L);
		this.vista = vista;
	}

	@Override
	public void consultarConsulta() {
		try {
			ResponseConsulta respuesta = this.modelo.getConsulta();
			this.vista.refrescarGridConsulta(respuesta.getItems());
			
		} catch (IOException e){
			e.printStackTrace();
		} 

	}

}
