package hn.clinica.views.consulta;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import hn.clinica.data.controller.CitasInteractorImpl;
import hn.clinica.data.controller.ConsultaInteractorImpl;
import hn.clinica.data.controller.MedicamentosInteractorImpl;
import hn.clinica.data.controller.PacientesInteractorImpl;
import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.Consulta;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.views.MainLayout;
import hn.clinica.views.citas.CitasViewModel;
import hn.clinica.views.medicamentos.MedicamentosViewModel;
import hn.clinica.views.pacientes.PacientesView;
import hn.clinica.views.pacientes.PacientesViewModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
	private TextField nombre = new TextField("Paciente");
	private TextField medicamento = new TextField("Medicamento");
	private TextField telefono = new TextField("Telefono");
	private TextField stocks = new TextField("Stock");
	private TextField fecha = new TextField("Fecha");

	private List<Consulta> consultas;
	// private Consulta consulta;
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
		nombre.setPlaceholder("");

		// componenetes de texfield filter
		identidad.setClearButtonVisible(true);
		identidad.setPrefixComponent(VaadinIcon.USER.create());
		identidad.setClearButtonVisible(true);
		// medicamento.setPrefixComponent(new Span("+504"));
		// telefono.setPrefixComponent(VaadinIcon.EYEDROPPER.create());
		stocks.setSuffixComponent(new Span("Uds"));
		nombre.setClearButtonVisible(true);
		// nombre.setValue("Nombre y Apeliido");

		// ACCION DE BOTONES
		Button resetBtn = new Button("Limpiar");
		// <theme-editor-local-classname>
		resetBtn.addClassName("consulta-view-button-1");
		resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
		resetBtn.addClickListener(e -> {
			clearFiltersAndRefreshGrid();
		});

		// ACCION DE BUSCAR POR MEDIO DEL FILTRO
		Button searchBtn = new Button("Buscar");
		// <theme-editor-local-classname>
		searchBtn.addClassName("consulta-view-button-2");
		searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
		searchBtn.addClickListener(e -> {
			applyFiltersAndRefreshGrid();
			//List<Consulta> filteredConsultas = filtrarConsultas();
			//refrescarGridConsulta(filteredConsultas);
		});

		Div actions = new Div(resetBtn, searchBtn);
		// <theme-editor-local-classname>
		actions.addClassName("consulta-view-div-1");
		actions.addClassName(LumoUtility.Gap.SMALL);
		actions.addClassName("actions");

		add(identidad, nombre, medicamento, telefono, stocks, fecha, actions);

		layout.setSizeFull();
		layout.setPadding(false);
		layout.setSpacing(false);
		add(layout, layout1);

		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() != null) {
				UI.getCurrent().navigate(String.format(CONSULTA_EDIT_ROUTE_TEMPLATE, event.getValue().getIdentidad()));
			} else {
				clearForm();
				UI.getCurrent().navigate(ConsultaView.class);
			}
		});

	}

	public void beforeEnter(BeforeEnterEvent event) {
		Optional<String> consultaId = event.getRouteParameters().get(CONSULTA_ID);
		boolean encontrado = false;
		if (consultaId.isPresent()) {
			for (Consulta e : this.consultas) {
				if (e.getIdentidad().equals(consultaId.get())) {
					populateForm(e);
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
		grid.select(null);
		grid.getDataProvider().refreshAll();
	}

	private void clearForm() {
		populateForm(null);
	}

	private void populateForm(Consulta e) {

		if (e == null) {
			this.identidad.setValue("");
			this.nombre.setValue("");
			this.medicamento.setValue("");
			this.telefono.setValue("");
			this.stocks.setValue("");
			this.fecha.setValue("");
		} else {
			this.identidad.setValue(e.getIdentidad());
			this.nombre.setValue(e.getNombre());
			this.medicamento.setValue(e.getMedicamento());
			this.telefono.setValue(e.getTelefono());
			this.stocks.setValue(e.getStocks());
			this.fecha.setValue(e.getFecha());
		}

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
		public Predicate toPredicate(Root<Consulta> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
			List<Predicate> predicates = new ArrayList<>();

			if (!nombre.isEmpty()) {
				String lowerCaseFilter = nombre.getValue().toLowerCase();
				Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
						lowerCaseFilter + "%");
				Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
						lowerCaseFilter + "%");
				predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
			}
			/*if (!identidad.isEmpty()) {
				String databaseColumn = "phone";
				String ignore = "- ()";

				String lowerCaseFilter = ignoreCharacters(ignore, identidad.getValue().toLowerCase());
				Predicate phoneMatch = criteriaBuilder.like(
						ignoreCharacters(ignore, criteriaBuilder, criteriaBuilder.lower(root.get(databaseColumn))),
						"%" + lowerCaseFilter + "%");
				predicates.add(phoneMatch);
				/*
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
			}
		
		}*/
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
		String nombreFilter = nombre.getValue().trim();
		String medicamentoFilter = medicamento.getValue().trim();
		String telefonoFilter = telefono.getValue().trim();
		String stocksFilter = stocks.getValue().trim();
		String fechaFilter = fecha.getValue().trim();

		// Si todos los campos de filtro están vacíos, mostrar todos los datos sin
		// filtro
		/*if (identidadFilter.isEmpty() && nombreFilter.isEmpty() && medicamentoFilter.isEmpty()
				&& telefonoFilter.isEmpty() && stocksFilter.isEmpty() && fechaFilter.isEmpty()) {
			filteredConsultas.add(prueba);

		}*/
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

	private boolean cumpleConFiltros(Consulta consulta, String identidadFilter, String nombreFilter,
			String medicamentoFilter, String telefonoFilter, String stocksFilter, String fechaFilter) {
		return consulta.getIdentidad().toLowerCase().contains(identidadFilter.toLowerCase())
				&& consulta.getNombre().toLowerCase().contains(nombreFilter.toLowerCase())
				&& consulta.getMedicamento().toLowerCase().contains(medicamentoFilter.toLowerCase())
				&& consulta.getTelefono().toLowerCase().contains(telefonoFilter.toLowerCase())
				&& consulta.getStocks().toLowerCase().contains(stocksFilter.toLowerCase())
				&& consulta.getFecha().toLowerCase().contains(fechaFilter.toLowerCase());
	}
	
	private void applyFiltersAndRefreshGrid() {
		filteredConsultas = new ArrayList<>();
	    filteredConsultas = filtrarConsultas();
	    refrescarGridConsulta(filteredConsultas);
	}
	
	private void clearFiltersAndRefreshGrid() {
	    identidad.clear();
	    nombre.clear();
	    medicamento.clear();
	    telefono.clear();
	    stocks.clear();
	    fecha.clear();
	    //refrescarGridConsulta(consultas);
	    this.controlador.consultarConsultas();
	}

	private Component createGrid() {

		grid = new Grid<>(Consulta.class, false);
		grid.addColumn("identidad").setAutoWidth(true).setHeader("IDENTIDAD");
		grid.addColumn("nombre").setAutoWidth(true).setHeader("PACIENTE");
		grid.addColumn("telefono").setAutoWidth(true).setHeader("TELEFONO");
		grid.addColumn("medicamento").setAutoWidth(true).setHeader("MEDICAMENTO");
		grid.addColumn("stocks").setAutoWidth(true).setHeader("STOCKS");
		grid.addColumn("fecha").setAutoWidth(true).setHeader("FECHA DE CITA");

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

	/*
	 * @Override public void mostrarMensajeCreacion(boolean Exito) { // TODO
	 * Auto-generated method stub
	 * 
	 * }
	 * 
	 * 
	 * @Override public void refrescarGridPacientes(List<Pacientes> pacientes) { //
	 * TODO Auto-generated method stub
	 * 
	 * }
	 */

}