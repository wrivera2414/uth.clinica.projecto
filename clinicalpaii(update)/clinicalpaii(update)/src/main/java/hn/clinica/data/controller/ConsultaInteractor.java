package hn.clinica.data.controller;

import hn.clinica.data.entity.Consulta;

public interface ConsultaInteractor {
	void consultarConsultas();
	void actualizarConsulta(Consulta actualizada);
	void consultarMedicamentos();
}
