package hn.clinica.views.consulta;

import java.util.List;

import com.vaadin.flow.router.BeforeEnterEvent;
import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.Consulta;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.data.entity.Pacientes;

public interface ConsultaViewModel {

	void refrescarGridConsulta(List<Consulta> consulta);
	void beforeEnter(BeforeEnterEvent event);

    void refrescarComboBoxPacientes(List<Pacientes> items);
    void refrescarComboBoxMedicamentos(List<Medicamentos> items);
    void mostrarMensajeCreacion(boolean respuesta);
}
