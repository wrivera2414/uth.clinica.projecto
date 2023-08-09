package hn.clinica.views.medicamentos;
import java.util.List;


import hn.clinica.data.entity.Medicamentos;

public interface MedicamentosViewModel {
	
	void refrescarGridMedicamentos(List<Medicamentos> medicamento);
	void mostrarMensajeCreacion(boolean exito);
	void mostrarMensajeEliminar(boolean exito);
}

