package hn.clinica.data.controller;

import hn.clinica.data.entity.Consulta;

public interface ConsultaInteractor {
	void consultarConsultas();
	void consultarPacientes();
	void consultarMedicamentos();
	void CrearConsulta(Consulta nuevo);
}
