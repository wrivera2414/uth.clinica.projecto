package hn.clinica.views.consulta;

import java.util.List;

import com.vaadin.flow.router.BeforeEnterEvent;

import hn.clinica.data.entity.Consulta;

public interface ConsultaViewModel {
	
	
	void refrescarGridConsulta(List<Consulta> consulta);

	void beforeEnter(BeforeEnterEvent event);


}
