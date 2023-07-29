//----->   MedicamentosView.java
package hn.clinica.views.medicamentos;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import hn.clinica.data.controller.MedicamentosInteractor;
import hn.clinica.data.controller.MedicamentosInteractorImpl;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.views.MainLayout;
import java.util.Collection;
import java.util.List;

@PageTitle("Medicamentos")
@Route(value = "medicamentos", layout = MainLayout.class)
@Uses(Icon.class)
public class MedicamentosView extends Div implements MedicamentosViewModel{

    private static Grid<Medicamentos> grid;

    private Filters filters;
    private List<Medicamentos> medicamentos;
    private MedicamentosInteractor controlador;
   
    
    
    //CONSTRUCTOR	
    public MedicamentosView() {
 
        setSizeFull();
        addClassNames("medicamentos-view");
        this.controlador = new MedicamentosInteractorImpl(this);
        filters = new Filters(() -> refrescarGridMedicamentos(medicamentos));
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
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

    public static class Filters extends Div implements BeforeEnterObserver,MedicamentosViewModel {

        private final TextField codigo = new TextField("Codigo");
        private final TextField nombre = new TextField("Nombre");
        private final TextField fabricante = new TextField("Fabricante");
        private final TextField principioa = new TextField("Principio");
        private final TextField fechav= new TextField("Fecha vencimiento");
        private final TextField stock= new TextField("Stock");
        private List<Medicamentos> medicamento;
        
        public Filters(Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            
            codigo.setPrefixComponent(VaadinIcon.QRCODE.create());
            nombre.setPrefixComponent(VaadinIcon.USER.create());
            fabricante.setPrefixComponent(VaadinIcon.PHONE_LANDLINE.create());
            fechav.setPrefixComponent(VaadinIcon.TIMER.create());
            principioa.setPrefixComponent(VaadinIcon.COMPILE.create());
            stock.setPrefixComponent(VaadinIcon.STOCK.create());
            
            // Action buttons
            Button resetBtn = new Button("Cancelar");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                codigo.clear();
                nombre.clear();
                fabricante.clear();
                principioa.clear();
                fechav.clear();
                stock.clear();
                
            });
            Button searchBtn = new Button("Buscar");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            searchBtn.addClickListener(e -> {
            	boolean encontrar = false;
            	if (!encontrar)
            	{
            		BuscarMedicamento();
            		
            	} else {
					
            		
            		
            	}});

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(codigo, nombre, fabricante, principioa, fechav, stock, actions);
        }

       private void BuscarMedicamento() {
			// Busca de Medicamentos

		}

	/* private Component createDateRangeFilter() {
            telefono.setPlaceholder("From");

            principioa.setPlaceholder("To");

            // For screen readers
            setAriaLabel(telefono, "From date");
            setAriaLabel(principioa, "To date");

            FlexLayout dateRangeComponent = new FlexLayout(telefono, new Text(" – "), principioa);
            dateRangeComponent.setAlignItems(FlexComponent.Alignment.BASELINE);
            dateRangeComponent.addClassName(LumoUtility.Gap.XSMALL);

            return dateRangeComponent;
        }

        private void setAriaLabel(DatePicker datePicker, String label) {
            datePicker.getElement().executeJs("const input = this.inputElement;" //
                    + "input.setAttribute('aria-label', $0);" //
                    + "input.removeAttribute('aria-labelledby');", label);
        }*/

      /*  @Override
        public Predicate toPredicate(Root<SamplePerson> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

                
            
            if (!codigo.isEmpty()) {
                String lowerCaseFilter = codigo.getValue().toLowerCase();
                Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        lowerCaseFilter + "%");
                Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                        lowerCaseFilter + "%");
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
            }
            if (!nombre.isEmpty()) {
                String databaseColumn = "phone";
                String ignore = "- ()";

                String lowerCaseFilter = ignoreCharacters(ignore, nombre.getValue().toLowerCase());
                Predicate phoneMatch = criteriaBuilder.like(
                        ignoreCharacters(ignore, criteriaBuilder, criteriaBuilder.lower(root.get(databaseColumn))),
                        "%" + lowerCaseFilter + "%");
                predicates.add(phoneMatch);

            }
            if (telefono.getValue() != null) {
                String databaseColumn = "dateOfBirth";
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(databaseColumn),
                        criteriaBuilder.literal(telefono.getValue())));
            }
            if (principioa.getValue() != null) {
                String databaseColumn = "dateOfBirth";
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.literal(principioa.getValue()),
                        root.get(databaseColumn)));
            }
            if (!fechav.isEmpty()) {
                String databaseColumn = "occupation";
                List<Predicate> occupationPredicates = new ArrayList<>();
                for (String occupation : fechav.getValue()) {
                    occupationPredicates
                            .add(criteriaBuilder.equal(criteriaBuilder.literal(occupation), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(occupationPredicates.toArray(Predicate[]::new)));
            }
            if (!stock.isEmpty()) {
                String databaseColumn = "role";
                List<Predicate> rolePredicates = new ArrayList<>();
                for (String stock  : stock.getValue()) {
                    rolePredicates.add(criteriaBuilder.equal(criteriaBuilder.literal(role), root.get(databaseColumn)));
                }
                predicates.add(criteriaBuilder.or(rolePredicates.toArray(Predicate[]::new)));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }*/

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refrescarGridMedicamentos(List<Medicamentos> medicamento) {
		// TODO Auto-generated method stub
		//Este Metodo refresca el Grid
		
				Collection<Medicamentos> items = medicamento;
				grid.setItems(items);
				this.medicamento = medicamento;
	}

    }

    private Component createGrid() {

        grid = new Grid<>(Medicamentos.class, false);
        grid.addColumn("codigo").setAutoWidth(true).setHeader("Codigo");
        grid.addColumn("nombre").setAutoWidth(true).setHeader("Nombre");
        grid.addColumn("fabricante").setAutoWidth(true).setHeader("Fabricante");
        grid.addColumn("principioa").setAutoWidth(true).setHeader("Principio Activo");
        grid.addColumn("fechav").setAutoWidth(true).setHeader("Fecha vencimiento");
        grid.addColumn("stock").setAutoWidth(true).setHeader("Stok");
        
        this.controlador.consultarMedicamentos();

     //   grid.addColumn("role").setAutoWidth(true);

        
        /*grid.setItems(query -> samplePersonService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                filters).stream());*/
        
        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }
    
    

	@Override
	public void refrescarGridMedicamentos(List<Medicamentos> medicamento) {
		Collection<Medicamentos> items = medicamento;
		grid.setItems(items);
		this.medicamentos = medicamento;
	}
    
    


}
