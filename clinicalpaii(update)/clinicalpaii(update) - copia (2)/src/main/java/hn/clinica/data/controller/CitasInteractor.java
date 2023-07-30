package hn.clinica.data.controller;

import hn.clinica.data.entity.Citas;

public interface CitasInteractor {
	
	void consultarCitas();
	void crearNuevaCitas(Citas nuevo);
}
