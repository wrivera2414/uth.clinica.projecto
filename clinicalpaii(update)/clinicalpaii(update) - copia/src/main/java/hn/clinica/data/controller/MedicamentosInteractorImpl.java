package hn.clinica.data.controller;
import java.io.IOException;

import hn.clinica.data.entity.CLINICARepositoryImpl;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.data.entity.ResponseMedicamento;
import hn.clinica.views.medicamentos.MedicamentosViewModel;

public class MedicamentosInteractorImpl implements MedicamentosInteractor {
	
	private CLINICARepositoryImpl modelo;
	
	private MedicamentosViewModel vista;
	
	
	public MedicamentosInteractorImpl(MedicamentosViewModel vista) 
	{
		super();
		this.modelo = CLINICARepositoryImpl.getInstance("https://apex.oracle.com/", 60000L);
		this.vista = vista;
	}

	@Override
		public void consultarMedicamentos() {
		try {
			ResponseMedicamento respuesta = this.modelo.getMedicamento();
			this.vista.refrescarGridMedicamentos(respuesta.getItems());
			
		} catch (IOException e){
			e.printStackTrace();
		} 
		
	}

	@Override
	public void crearNuevoMedicamento(Medicamentos nuevo) {
		try {
			boolean respuesta = this.modelo.crearMedicamento(nuevo);
			this.vista.mostrarMensajeCreacion(respuesta);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void eliminarMedicamento(String idMed) {
		try {
			boolean respuesta = this.modelo.borrarMedicamento(idMed);
			this.vista.mostrarMensajeEliminar(respuesta);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

}

