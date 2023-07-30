package hn.clinica.views.consulta;

import java.util.List;

import com.vaadin.flow.router.BeforeEnterEvent;
import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.data.entity.Pacientes;

public interface ConsultaViewModel {

	void refrescarGridPacientes(List<Pacientes> pacientes);
	
	void beforeEnter(BeforeEnterEvent event);


}
