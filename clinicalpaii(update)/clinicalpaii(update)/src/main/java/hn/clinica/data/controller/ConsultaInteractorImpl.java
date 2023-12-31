package hn.clinica.data.controller;

import java.io.IOException;

import hn.clinica.data.entity.CLINICARepositoryImpl;
import hn.clinica.data.entity.Consulta;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.data.entity.ResponseCitas;
import hn.clinica.data.entity.ResponseConsulta;
import hn.clinica.data.entity.ResponseMedicamento;
import hn.clinica.data.entity.ResponsePacientes;
import hn.clinica.views.citas.CitasViewModel;
import hn.clinica.views.consulta.ConsultaViewModel;
import hn.clinica.views.medicamentos.MedicamentosViewModel;
import hn.clinica.views.pacientes.PacientesViewModel;

public class ConsultaInteractorImpl implements ConsultaInteractor {

	private CLINICARepositoryImpl modelo;

	private ConsultaViewModel vista;

	public ConsultaInteractorImpl(ConsultaViewModel vista) {
		super();
		this.modelo = CLINICARepositoryImpl.getInstance("https://apex.oracle.com/", 60000L);
		this.vista = vista;
	}

	@Override
	public void consultarConsultas() {
		try {
			ResponseConsulta respuesta = this.modelo.getConsulta();
			this.vista.refrescarGridConsulta(respuesta.getItems());
			;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Consulta para llenar ComboBox con pacientes
	@Override
	public void consultarPacientes() {
		try {
			ResponsePacientes respuesta = this.modelo.getPacientes();
			this.vista.refrescarComboBoxPacientes(respuesta.getItems());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Consulta para llenar ComboBox con Medicamentos
	@Override
	public void consultarMedicamentos() {
		try {
			ResponseMedicamento respuesta = this.modelo.getMedicamento();
			this.vista.refrescarComboBoxMedicamentos(respuesta.getItems());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void CrearConsulta(Consulta nuevo) {
		try {
			boolean respuesta = this.modelo.crearNuevaConsulta(nuevo);
			this.vista.mostrarMensajeCreacion(respuesta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
