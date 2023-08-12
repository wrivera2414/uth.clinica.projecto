package hn.clinica.data.controller;

import hn.clinica.data.entity.Medicamentos;

public interface MedicamentosInteractor {

	void consultarMedicamentos();
	void crearNuevoMedicamento(Medicamentos nuevo);
	void eliminarMedicamento(String idMed);
}
