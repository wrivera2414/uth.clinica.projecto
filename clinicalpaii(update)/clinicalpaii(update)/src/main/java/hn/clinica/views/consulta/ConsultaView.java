package hn.clinica.views.consulta;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import hn.clinica.data.controller.ConsultaInteractorImpl;
import hn.clinica.data.entity.CitasDataReport;
import hn.clinica.data.entity.Consulta;
import hn.clinica.data.entity.ConsultaReporte;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.data.service.ReportGenerator;
import hn.clinica.views.MainLayout;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@PageTitle("CONSULTA")
@Route(value = "consulta/:consultaID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class ConsultaView extends Div implements ConsultaViewModel {

	private static final String CONSULTA_ID = "consultaID";
	private final String CONSULTA_EDIT_ROUTE_TEMPLATE = "consulta/%s/edit";
	private Grid<Consulta> grid;
	private Filters filters;
	private TextField identidad = new TextField("Identidad");
	// private TextField nombre = new TextField("Paciente");
	// private TextField medicamento = new TextField("Medicamento");
	private TextField telefono = new TextField("Telefono");
	private TextField stocks = new TextField("Stock");
	private TextField fecha = new TextField("Fecha");
	private ComboBox<Pacientes> comboBox_Paciente = new ComboBox<>("Pacientes");
	private ComboBox<Medicamentos> comboBox_medicamentos = new ComboBox<>("Medicamentos");
	private List<Pacientes> consulta_combobox_paciente;
	private List<Medicamentos> consulta_combobox_medicamentos;
	private List<Consulta> consultas;
	private Consulta consulta_entity;

	List<Consulta> filteredConsultas;
	private ConsultaInteractorImpl controlador;

	// Contructor
	public ConsultaView() {

		consultas = new ArrayList<>();
		this.controlador = new ConsultaInteractorImpl(this);

		setSizeFull();
		addClassNames("consulta-view");

		filters = new Filters(() -> refrescarGridConsulta(consultas));
		HorizontalLayout layout1 = new HorizontalLayout(filters, createMobileFilters());
		VerticalLayout layout = new VerticalLayout(createGrid());
		// nombre.setPlaceholder("");

		// componenetes de texfield filter
		identidad.setClearButtonVisible(true);
		identidad.setPrefixComponent(VaadinIcon.USER.create());
		identidad.setClearButtonVisible(true);
		identidad.addClassName(Margin.Right.LARGE);
		// medicamento.setPrefixComponent(new Span("+504"));
		// telefono.setPrefixComponent(VaadinIcon.EYEDROPPER.create());
		stocks.setSuffixComponent(new Span("Uds"));
		stocks.addClassName(Margin.Right.LARGE);
		// nombre.setClearButtonVisible(true);
		// nombre.setValue("Nombre y Apeliido");

		telefono.addClassName(Margin.Right.LARGE);
		fecha.addClassName(Margin.Right.LARGE);

		this.controlador.consultarPacientes();
		comboBox_Paciente.setItems(consulta_combobox_paciente);
		comboBox_Paciente.addClassName(Margin.Right.LARGE);
		comboBox_Paciente.setItemLabelGenerator(Pacientes::getNombre);

		this.controlador.consultarMedicamentos();
		comboBox_medicamentos.setItems(consulta_combobox_medicamentos);
		comboBox_medicamentos.addClassName(Margin.Right.LARGE);
		comboBox_medicamentos.setItemLabelGenerator(Medicamentos::getNombre);

		// ACCION DE BOTONES
		Button resetBtn = new Button("Limpiar");
		// <theme-editor-local-classname>
		resetBtn.addClassName("consulta-view-button-1");
		resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
		resetBtn.addClickListener(e -> {
			clearFiltersAndRefreshGrid();
		});

		// ACCION DE BUSCAR POR MEDIO DEL FILTRO
		Button crearBtn = new Button("Crear");
		// <theme-editor-local-classname>
		crearBtn.addClassName("consulta-view-button-2");
		crearBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		crearBtn.addClickListener(e -> {

			if (this.consulta_entity == null) {
				this.consulta_entity = new Consulta();

				this.consulta_entity.setIdentidad(this.identidad.getValue());
				this.consulta_entity.setNombre(this.comboBox_Paciente.getValue().getNombre());
				this.consulta_entity.setTelefono(this.telefono.getValue());
				this.consulta_entity.setMedicamento(this.comboBox_medicamentos.getValue().getNombre());
				this.consulta_entity.setStocks(this.stocks.getValue());
				this.controlador.CrearConsulta(consulta_entity);
			}
			clearForm();
			refreshGrid();
			// applyFiltersAndRefreshGrid();
			// List<Consulta> filteredConsultas = filtrarConsultas();
			// refrescarGridConsulta(filteredConsultas);
		});

		Div actions = new Div(resetBtn, crearBtn);
		// <theme-editor-local-classname>
		actions.addClassName("consulta-view-div-1");
		actions.addClassName(LumoUtility.Gap.SMALL);
		actions.addClassName("actions");

		add(comboBox_Paciente, comboBox_medicamentos, identidad, /* nombre, medicamento, */ telefono, stocks, fecha,
				actions);

		layout.setSizeFull();
		layout.setPadding(false);
		layout.setSpacing(false);
		add(layout, layout1);

		 GridContextMenu<Consulta> menu = grid.addContextMenu();
	    	menu.addItem("Generar Reporte", event -> {
	    		if(this.consultas.isEmpty()) {
	        		Notification.show("No hay datos para generar el reporte");
	        	}else {
	        		Notification.show("Generando reporte PDF...");
	        		generarReporteConsulta();
	        	}
	    	});

		
		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(CONSULTA_EDIT_ROUTE_TEMPLATE,
						event.getValue().getIdentidad()));
				populateForm2(event.getValue());
			} else {
				clearForm();
				UI.getCurrent().navigate(ConsultaView.class);
			}
		});

		comboBox_medicamentos.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				Medicamentos selectedMedicamento = event.getValue();
				actualizarCamposConMedicamento(selectedMedicamento);
			}
		});

		comboBox_Paciente.addValueChangeListener(event -> {
			if (event.getValue() != null) {
				Pacientes selectedPaciente = event.getValue();
				actualizarCamposConPaciente(selectedPaciente);
			}
		});

	}


	private void generarReporteConsulta() {
		ReportGenerator generador = new ReportGenerator(); 
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("LOGO_DIR", "loguito.png");
		parametros.put("LOGO_BAR", "loguito.png");
		ConsultaReporte datasource = new ConsultaReporte();
		datasource.setData(consultas);
		String generado = generador.generarReportePDF("Reporte_Consulta", parametros, datasource);
	    if(generado != null) {
	    	StreamResource resource = new StreamResource("Reporte de Orden Consulta.pdf", () -> {
                try {
                    return new FileInputStream(generado);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            });
	    	
	    	//String ubicacion = generador.getUbicacion();
	    	Anchor url = new Anchor(resource, "Abrir reporte PDF");
			url.setTarget("_Blank"); 
			add(url);
			
			Notification notificacion = new Notification(url);
			notificacion.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notificacion.setDuration(20000);
			notificacion.open();
		}else {
			Notification notificacion = new Notification("Ocurrió un problema al generar el reporte");
			notificacion.addThemeVariants(NotificationVariant.LUMO_ERROR);
			notificacion.setDuration(10000);
			notificacion.open();
	    }
	
	}


	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		Optional<String> consultaId = event.getRouteParameters().get(CONSULTA_ID);
		boolean encontrado = false;
		if (consultaId.isPresent()) {
			for (Consulta e : this.consultas) {
				if (e.getIdentidad().equals(consultaId.get())) {
					populateForm2(e);
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				Notification.show(
						String.format("El empleado con identidad= %s", consultaId.get() + "No fue encontrado"), 3000,
						Notification.Position.BOTTOM_START);
				refreshGrid();
				event.forwardTo(ConsultaView.class);

			}
		}
	}

	private void refreshGrid() {
		this.controlador.consultarConsultas();
		grid.select(null);
		grid.getDataProvider().refreshAll();
	}

	private void clearForm() {
		populateForm2(null);
	}

	/*
	 * private void populateForm(Consulta e) {
	 * if (e == null) {
	 * this.identidad.setValue("");
	 * this.nombre.setValue("");
	 * this.medicamento.setValue("");
	 * this.telefono.setValue("");
	 * this.stocks.setValue("");
	 * this.fecha.setValue("");
	 * this.comboBox_medicamentos.setValue(null);
	 * this.comboBox_Paciente.setValue(null);
	 * } else {
	 * this.identidad.setValue(e.getIdentidad());
	 * this.nombre.setValue(e.getNombre());
	 * this.medicamento.setValue(e.getMedicamento());
	 * this.telefono.setValue(e.getTelefono());
	 * this.stocks.setValue(e.getStocks());
	 * this.fecha.setValue(e.getFecha());
	 * this.comboBox_medicamentos.setValue(buscarMedicamentosSeleccionado(e.
	 * getMedicamento()));
	 * this.comboBox_Paciente.setValue(buscarPacientesSeleccionado(e.getIdentidad())
	 * );
	 * }
	 * }
	 */

	private void populateForm2(Consulta e) {
		if (e == null) {
			this.identidad.setValue("");
			// this.nombre.setValue("");
			// this.medicamento.setValue("");
			this.telefono.setValue("");
			this.stocks.setValue("");
			this.fecha.setValue("");
			this.comboBox_medicamentos.setValue(null);
			this.comboBox_Paciente.setValue(null);
		} else {
			this.comboBox_medicamentos.setValue(buscarMedicamentosSeleccionado(e.getMedicamento()));
			this.comboBox_Paciente.setValue(buscarPacientesSeleccionado(e.getIdentidad()));
		}
	}

	private Medicamentos buscarMedicamentosSeleccionado(String medicamento) {
		Medicamentos seleccionado = new Medicamentos();
		for (Medicamentos v : consulta_combobox_medicamentos) {
			if (v.getNombre().toUpperCase().equals(medicamento.toUpperCase())) {
				seleccionado = v;
				break;
			}
		}
		return seleccionado;
	}

	private Pacientes buscarPacientesSeleccionado(String pacientes) {
		Pacientes seleccionado = new Pacientes();
		for (Pacientes v : consulta_combobox_paciente) {
			if (v.getIdentidad().toUpperCase().equals(pacientes)) {
				seleccionado = v;
				break;
			}
		}
		return seleccionado;
	}

	private void actualizarCamposConPaciente(Pacientes paciente) {
		identidad.setValue(paciente.getIdentidad());
		// nombre.setValue(paciente.getNombre());
		telefono.setValue(paciente.getTelefono());
	}

	private void actualizarCamposConMedicamento(Medicamentos medicament) {
		// medicamento.setValue(medicament.getNombre());
		stocks.setValue(medicament.getStock());
		fecha.setValue(medicament.getFechav());
		// Actualiza otros campos de texto según sea necesario
	}

	private HorizontalLayout createMobileFilters() {
		// Mobile version
		HorizontalLayout mobileFilters = new HorizontalLayout();
		mobileFilters.setWidthFull();
		mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
				LumoUtility.AlignItems.CENTER);
		mobileFilters.addClassName("mobile-filters");

		Icon mobileIcon = new Icon("lumo", "plus");
		Span filtersHeading = new Span("Filters");
		mobileFilters.add(mobileIcon, filtersHeading);
		mobileFilters.setFlexGrow(1, filtersHeading);
		mobileFilters.addClickListener(e -> {
			if (filters.getClassNames().contains("visible")) {
				filters.removeClassName("visible");
				mobileIcon.getElement().setAttribute("icon", "lumo:plus");
			} else {
				filters.addClassName("visible");
				mobileIcon.getElement().setAttribute("icon", "lumo:minus");
			}
		});
		return mobileFilters;
	}

	public static class Filters extends Div {

		public Filters(Runnable onSearch) {
			setWidthFull();
			addClassName("filter-layout");
			addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
					LumoUtility.BoxSizing.BORDER);

		}/*
			 * public Predicate toPredicate(Root<Consulta> root, CriteriaQuery<?> query,
			 * CriteriaBuilder criteriaBuilder) {
			 * List<Predicate> predicates = new ArrayList<>();
			 * 
			 * if (!nombre.isEmpty()) {
			 * String lowerCaseFilter = nombre.getValue().toLowerCase();
			 * Predicate firstNameMatch =
			 * criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
			 * lowerCaseFilter + "%");
			 * Predicate lastNameMatch =
			 * criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
			 * lowerCaseFilter + "%");
			 * predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
			 * }
			 * /*if (!identidad.isEmpty()) {
			 * String databaseColumn = "phone";
			 * String ignore = "- ()";
			 * 
			 * String lowerCaseFilter = ignoreCharacters(ignore,
			 * identidad.getValue().toLowerCase());
			 * Predicate phoneMatch = criteriaBuilder.like(
			 * ignoreCharacters(ignore, criteriaBuilder,
			 * criteriaBuilder.lower(root.get(databaseColumn))),
			 * "%" + lowerCaseFilter + "%");
			 * predicates.add(phoneMatch);
			 * /*
			 * } if (Fecha.getValue() != null) { String databaseColumn = "dateOfBirth";
			 * predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(databaseColumn),
			 * criteriaBuilder.literal(Fecha.getValue()))); }
			 * 
			 * if (!occupations.isEmpty()) { String databaseColumn = "occupation";
			 * List<Predicate> occupationPredicates = new ArrayList<>(); for (String
			 * occupation : occupations.getValue()) { occupationPredicates
			 * .add(criteriaBuilder.equal(criteriaBuilder.literal(occupation),
			 * root.get(databaseColumn))); }
			 * predicates.add(criteriaBuilder.or(occupationPredicates.toArray(Predicate[]::
			 * new))); }
			 * 
			 * return criteriaBuilder.and(predicates.toArray(Predicate[]::new)); }
			 * 
			 *
			 * }
			 * 
			 * }
			 */
	}

	@Override
	public void refrescarGridConsulta(List<Consulta> consulta) {
		Collection<Consulta> items = consulta;
		grid.setItems(items);
		this.consultas = consulta;
	}

	private List<Consulta> filtrarConsultas() {
		List<Consulta> filteredConsultas = new ArrayList<>();
		String identidadFilter = identidad.getValue().trim();
		String nombreFilter = comboBox_Paciente.getValue().getNombre().trim();
		String medicamentoFilter = comboBox_medicamentos.getValue().getNombre().trim();
		String telefonoFilter = telefono.getValue().trim();
		String stocksFilter = stocks.getValue().trim();
		String fechaFilter = fecha.getValue().trim();

		// Si todos los campos de filtro están vacíos, mostrar todos los datos sin
		// filtro
		/*
		 * if (identidadFilter.isEmpty() && nombreFilter.isEmpty() &&
		 * medicamentoFilter.isEmpty()
		 * && telefonoFilter.isEmpty() && stocksFilter.isEmpty() &&
		 * fechaFilter.isEmpty()) {
		 * filteredConsultas.add(prueba);
		 * 
		 * }
		 */
		this.controlador.consultarConsultas();
		// Aquí realiza la lógica de filtrado según tus necesidades
		for (Consulta consulta : consultas) {
			if (cumpleConFiltros(consulta, identidadFilter, nombreFilter, medicamentoFilter, telefonoFilter,
					stocksFilter, fechaFilter)) {
				filteredConsultas.add(consulta);
			}
		}

		return filteredConsultas;
	}

	/*
	 * private boolean cumpleConFiltros(Consulta consulta, String identidadFilter,
	 * String nombreFilter,
	 * String medicamentoFilter, String telefonoFilter, String stocksFilter, String
	 * fechaFilter) {
	 * return
	 * consulta.getIdentidad().toLowerCase().contains(identidadFilter.toLowerCase())
	 * && consulta.getNombre().toLowerCase().contains(nombreFilter.toLowerCase())
	 * && consulta.getMedicamento().toLowerCase().contains(medicamentoFilter.
	 * toLowerCase())
	 * &&
	 * consulta.getTelefono().toLowerCase().contains(telefonoFilter.toLowerCase())
	 * && consulta.getStocks().toLowerCase().contains(stocksFilter.toLowerCase())
	 * && consulta.getFecha().toLowerCase().contains(fechaFilter.toLowerCase());
	 * }
	 */

	private boolean cumpleConFiltros(Consulta consulta, String identidadFilter, String nombreFilter,
			String medicamentoFilter, String telefonoFilter, String stocksFilter, String fechaFilter) {
		return (identidadFilter.isEmpty()
				|| consulta.getIdentidad().toLowerCase().contains(identidadFilter.toLowerCase()))
				&& (nombreFilter.isEmpty() || consulta.getNombre().toLowerCase().contains(nombreFilter.toLowerCase()))
				&& (medicamentoFilter.isEmpty()
						|| consulta.getMedicamento().toLowerCase().contains(medicamentoFilter.toLowerCase()))
				&& (telefonoFilter.isEmpty()
						|| consulta.getTelefono().toLowerCase().contains(telefonoFilter.toLowerCase()))
				&& (stocksFilter.isEmpty() || consulta.getStocks().toLowerCase().contains(stocksFilter.toLowerCase()))
				&& (fechaFilter.isEmpty() || consulta.getFecha().toLowerCase().contains(fechaFilter.toLowerCase()));
	}

	private void applyFiltersAndRefreshGrid() {
		filteredConsultas = new ArrayList<>();
		filteredConsultas = filtrarConsultas();
		refrescarGridConsulta(filteredConsultas);
	}

	private void clearFiltersAndRefreshGrid() {
		identidad.clear();
		// nombre.clear();
		// medicamento.clear();
		telefono.clear();
		stocks.clear();
		fecha.clear();
		comboBox_Paciente.clear();
		comboBox_medicamentos.clear();
		// refrescarGridConsulta(consultas);
		this.controlador.consultarConsultas();
	}

	private Component createGrid() {

		grid = new Grid<>(Consulta.class, false);
		grid.addColumn("identidad").setAutoWidth(true).setHeader("IDENTIDAD");
		grid.addColumn("nombre").setAutoWidth(true).setHeader("PACIENTE");
		grid.addColumn("telefono").setAutoWidth(true).setHeader("TELEFONO");
		grid.addColumn("medicamento").setAutoWidth(true).setHeader("MEDICAMENTO");
		grid.addColumn("stocks").setAutoWidth(true).setHeader("STOCKS");
		// grid.addColumn("fecha").setAutoWidth(true).setHeader("FECHA DE CITA");

		this.controlador.consultarConsultas();

		/*
		 * grid.setItems(query -> samplePersonService.list(
		 * PageRequest.of(query.getPage(), query.getPageSize(),
		 * VaadinSpringDataHelpers.toSpringDataSort(query)), filters).stream());
		 */

		grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
		grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

		return grid;
	}

	@Override
	public void refrescarComboBoxPacientes(List<Pacientes> items) {
		consulta_combobox_paciente = items;
	}

	@Override
	public void refrescarComboBoxMedicamentos(List<Medicamentos> items) {
		consulta_combobox_medicamentos = items;
	}

	@Override
	public void mostrarMensajeCreacion(boolean respuesta) {
		String mensajeMostrar = "Registro Exitoso!";
		if (!respuesta) {
			mensajeMostrar = "Error al Registrar";
		}
		Notification.show(mensajeMostrar);
	}

}