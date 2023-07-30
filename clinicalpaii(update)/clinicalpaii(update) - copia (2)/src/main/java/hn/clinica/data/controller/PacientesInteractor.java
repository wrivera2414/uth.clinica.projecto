package hn.clinica.data.controller;

import hn.clinica.data.entity.Pacientes;

public interface PacientesInteractor {
	
	void consultarPacientes();
	void crearPacientes(Pacientes nuevo);
}
