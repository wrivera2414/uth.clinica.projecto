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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import hn.clinica.data.controller.ConsultaInteractor;
import hn.clinica.data.controller.ConsultaInteractorImpl;
import hn.clinica.data.entity.Consulta;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.views.MainLayout;
import hn.clinica.views.pacientes.PacientesView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;

@PageTitle("Consulta")
@Route(value = "consulta", layout = MainLayout.class)
@Uses(Icon.class)
public class ConsultaView extends Div implements ConsultaViewModel {
	
	
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

    private List<Consulta> consultas;
    private ConsultaInteractor controlador;
    


    
    	//Contructor 
    	public ConsultaView() {
    	consultas = new ArrayList<>();
    	this.controlador = new ConsultaInteractorImpl(this);
    	
        
        setSizeFull();
        addClassNames("consulta-view");
        
        
        
        filters = new Filters(() -> refrescarGridConsulta(consultas));
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
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
        searchBtn.addClickListener(e -> this.consultas.get(0).getIdentidad());

        Div actions = new Div(resetBtn, searchBtn);
        actions.addClassName(LumoUtility.Gap.SMALL);
        actions.addClassName("actions");

        add(Identidad,Nombre, Telefono,Medicamentos,Stock,Fecha,actions);
        
        
        
        
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
        
        
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CONSULTA_EDIT_ROUTE_TEMPLATE, event.getValue().getIdentidad()));
            } else {
                UI.getCurrent().navigate(PacientesView.class);
            }
        });
        

    }
    	
    	
        public void beforeEnter(BeforeEnterEvent event) {
    	        Optional<String> consultaId = event.getRouteParameters().get(CONSULTA_ID);
    	        boolean encontrado = false;
    	       if (consultaId.isPresent()){
    	        	for (Consulta e: this.consultas){
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


	private void populateForm(Consulta value) {

		
		if(value == null) 
    	{
    		this.Identidad.setValue("");	
		    this.Nombre.setValue("");	
		    this.Telefono.setValue("");	
		    this.Medicamentos.setValue("");	
		    this.Stock.setValue("");	
		    this.Fecha.setValue("");
    	}
    	else 
    	{
    		this.Identidad.setValue(value.getNombre());	
    	    this.Nombre.setValue(value.getIdentidad());	
    	    this.Telefono.setValue(value.getTelefono());	
    	    this.Medicamentos.setValue(value.getMedicamento());	 
    	    this.Stock.setValue(value.getStocks().toString());
    	    this.Fecha.setValue(value.getFecha());	
    	
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
      

      
      
      
        private String ignoreCharacters(String characters, String in) {
            String result = in;
            
            
            for (int i = 0; i < characters.length(); i++) {
                result = result.replace("" + characters.charAt(i), "");
            }
            return result;
        }
        

        private Expression<String> ignoreCharacters(String characters, CriteriaBuilder criteriaBuilder,
                Expression<String> inExpression) {
            Expression<String> expression = inExpression;
            
            for (int i = 0; i < characters.length(); i++) 
            {
                expression = criteriaBuilder.function("replace", String.class, expression,
                        criteriaBuilder.literal(characters.charAt(i)), criteriaBuilder.literal(""));
            }
            return expression;
        }
        
    }
    
    
    

    private Component createGrid() {
    	
    	
    	
        grid = new Grid<>(Consulta.class, false);
        grid.addColumn("identidad").setAutoWidth(true).setHeader("Identidad");
        grid.addColumn("nombre").setAutoWidth(true).setHeader("Nombre");
        grid.addColumn("telefono").setAutoWidth(true).setHeader("Telefono");
        grid.addColumn("medicamento").setAutoWidth(true).setHeader("Medicamentos");
        grid.addColumn("stocks").setAutoWidth(true).setHeader("Stocks");
        grid.addColumn("fecha").setAutoWidth(true).setHeader("Fecha");

        
        this.controlador.consultarConsulta();

        /*grid.setItems(query -> samplePersonService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                filters).stream());*/
        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }
    


	@Override
	public void refrescarGridConsulta(List<Consulta> consulta) {
		Collection<Consulta> items = consulta;
		grid.setItems(items);
		this.consultas = consulta;
		
	}

}
