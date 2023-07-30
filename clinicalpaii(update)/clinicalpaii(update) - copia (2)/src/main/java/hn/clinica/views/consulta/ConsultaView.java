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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@PageTitle("Consulta")
@Route(value = "consulta/:consultaID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class ConsultaView extends Div implements PacientesViewModel {
	
	
	private static final String CONSULTA_ID = "consultaID";
	private final String CONSULTA_EDIT_ROUTE_TEMPLATE = "consulta/%s/edit";
    private Grid<Consulta> grid;
    private Filters filters;
    private TextField Identidad = new TextField("Identidad");
    private  TextField Nombre = new TextField("Nombre");
    private  TextField Telefono = new TextField("Telefono");
    private  TextField Medicamentos = new TextField("Medicamentos");
    private  TextField Stock = new TextField("Stock");
    private  TextField Fecha = new TextField("Fecha");

    

    private List<Pacientes> pacientes;

    private PacientesInteractorImpl controlador;


    
    	//Contructor 
    	public ConsultaView() {
    		
    	pacientes = new ArrayList<>();
    	this.controlador = new PacientesInteractorImpl(this);
  
    	
        setSizeFull();
        addClassNames("consulta-view");
        
        
        
        filters = new Filters(() -> refrescarGridPacientes(pacientes));
        HorizontalLayout layout1 = new HorizontalLayout(filters,createMobileFilters());
        VerticalLayout layout = new VerticalLayout(createGrid());
        Nombre.setPlaceholder("");
        
        //componenetes de texfield filter
        Identidad.setClearButtonVisible(true);
        Identidad.setPrefixComponent(VaadinIcon.USER.create());
        Identidad.setClearButtonVisible(true);
        Telefono.setPrefixComponent(new Span("+504"));
        Medicamentos.setPrefixComponent(VaadinIcon.EYEDROPPER.create());
        Stock.setSuffixComponent(new Span("Uds"));
        Nombre.setClearButtonVisible(true);
        Nombre.setValue("Nombre y Apeliido");

        // ACCION DE BOTONES
        Button resetBtn = new Button("Limpiar");
        resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY,ButtonVariant.LUMO_ERROR);
        resetBtn.addClickListener(e -> {
            Nombre.clear();
            Telefono.clear();
            Fecha.clear();
            Medicamentos.clear();
            Stock.clear();
            //endDate.clear();
           // occupations.clear();
            //roles.clear();
        });
        

        //ACCION DE BUSCAR POR MEDIO DEL FILTRO
        Button searchBtn = new Button("Buscar");
        searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY,ButtonVariant.LUMO_SUCCESS);
        searchBtn.addClickListener(e -> {
        
        		
        });
        
        

        Div actions = new Div(resetBtn, searchBtn);
        actions.addClassName(LumoUtility.Gap.SMALL);
        actions.addClassName("actions");

        add(Identidad,Nombre, Telefono,Medicamentos,Stock,Fecha,actions);

        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout,layout1);
        
        
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
    	       if (consultaId.isPresent()){
    	        	for (Pacientes e: this.pacientes){
    	        		if(e.getIdentidad().equals(consultaId.get()))
    	        		{
    	                    populateForm(e);
    	                    encontrado = true;
    	                    break;
    	        		}	
    	        	}
    	        		if(!encontrado) 
    	        		{
    	        			 Notification.show(String.format("El empleado con identidad= %s", consultaId.get() + "No fue encontrado"),
    	                             3000, Notification.Position.BOTTOM_START);
    	                     refreshGrid();
    	                     event.forwardTo(PacientesView.class); 
    	        			
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

	private void populateForm(Pacientes e) {

		if(e == null) 
    	{
    		this.Identidad.setValue("");	
		    this.Nombre.setValue("");	
		    this.Telefono.setValue("");	

    	}
    	else 
    	{
    		this.Identidad.setValue(e.getNombre());	
    	    this.Nombre.setValue(e.getIdentidad());	
    	    this.Telefono.setValue(e.getTelefono());	

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
    

    

    public static class Filters extends Div{

        public Filters(Runnable onSearch) {
        	
        	
            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            
        }
        
        
        
        
      /*@Override
        public Predicate toPredicate(Root<Consulta> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();
            
            if (!Nombre.isEmpty()) {
                String lowerCaseFilter = Nombre.getValue().toLowerCase();
                Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        lowerCaseFilter + "%");
                Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                        lowerCaseFilter + "%");
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
            }
            if (!Telefono.isEmpty()) {
                String databaseColumn = "phone";
                String ignore = "- ()";

                String lowerCaseFilter = ignoreCharacters(ignore, Telefono.getValue().toLowerCase());
                Predicate phoneMatch = criteriaBuilder.like(
                        ignoreCharacters(ignore, criteriaBuilder, criteriaBuilder.lower(root.get(databaseColumn))),
                        "%" + lowerCaseFilter + "%");
                predicates.add(phoneMatch);

            }
            if (Fecha.getValue() != null) {
                String databaseColumn = "dateOfBirth";
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(databaseColumn),
                        criteriaBuilder.literal(Fecha.getValue())));
            }

           /*if (!occupations.isEmpty()) {
                String databaseColumn = "occupation";
                List<Predicate> occupationPredicates = new ArrayList<>();
                for (String occupation : occupations.getValue()) {
                    occupationPredicates
                            .add(criteriaBuilder.equal(criteriaBuilder.literal(occupation), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(occupationPredicates.toArray(Predicate[]::new)));
           }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }*/
 
        
    }
    
    
    

    private Component createGrid() {
    	
        grid = new Grid<>(Consulta.class, false);
        grid.addColumn("identidad").setAutoWidth(true).setHeader("Identidad");
        grid.addColumn("nombre").setAutoWidth(true).setHeader("Nombre Paciente");
        grid.addColumn("telefono").setAutoWidth(true).setHeader("Telefono");
        grid.addColumn("principioa").setAutoWidth(true).setHeader("Medicamento Resetado");
        grid.addColumn("stock").setAutoWidth(true).setHeader("Cantidad Resetada");
        grid.addColumn("fecha").setAutoWidth(true).setHeader("Fecha de Cita");
        
        
        
        this.controlador.consultarPacientes();
        
        /*grid.setItems(query -> samplePersonService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                filters).stream());*/
        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }
    

	@Override
	public void mostrarMensajeCreacion(boolean Exito) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void refrescarGridPacientes(List<Pacientes> pacientes) {
		// TODO Auto-generated method stub
		
	}




}
