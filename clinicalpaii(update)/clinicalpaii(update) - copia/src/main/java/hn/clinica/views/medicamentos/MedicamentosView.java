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
import com.vaadin.flow.component.notification.Notification;
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
import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.views.MainLayout;
import java.util.Collection;
import java.util.List;

@PageTitle("Medicamentos")
@Route(value = "medicamentos", layout = MainLayout.class)
@Uses(Icon.class)
public class MedicamentosView extends Div implements MedicamentosViewModel{

    private final TextField codigo = new TextField("Codigo");
    private final TextField nombre = new TextField("Nombre");
    private final TextField fabricante = new TextField("Fabricante");
    private final TextField principioa = new TextField("Principio");
    private final TextField fechav= new TextField("Fecha vencimiento");
    private final TextField stock= new TextField("Stock");
    private static Grid<Medicamentos> grid;
    private List<Medicamentos> medicamentos;
    private MedicamentosInteractor controlador;
    private Medicamentos medicamentoNuevo;
    
    
    //CONSTRUCTOR	
    public MedicamentosView() {
        setSizeFull();
        addClassNames("medicamentos-view");
        this.controlador = new MedicamentosInteractorImpl(this);
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), Filters(), createGrid());
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

        return mobileFilters;
    }

      private Div Filters() {
    	  Div container  = new Div();
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
            Button resetBtn = new Button("Eliminar");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
        		this.controlador.eliminarMedicamento(this.medicamentoNuevo.getCodigo());
        		this.controlador.consultarMedicamentos();
                codigo.clear();
                nombre.clear();
                fabricante.clear();
                principioa.clear();
                fechav.clear();
                stock.clear();
                
            });
            Button searchBtn = new Button("Insertar");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            searchBtn.addClickListener(e -> {
                if (this.medicamentoNuevo == null) {
                    //CEANDO REGISTRO 
                	this.medicamentoNuevo = new Medicamentos();
                	this.medicamentoNuevo.setCodigo(this.codigo.getValue());
                	this.medicamentoNuevo.setNombre(this.nombre.getValue());
                	this.medicamentoNuevo.setFabricante(this.fabricante.getValue());
                	this.medicamentoNuevo.setPrincipioa(this.principioa.getValue());
                	this.medicamentoNuevo.setFechav(this.fechav.getValue());
                	this.medicamentoNuevo.setStock(this.stock.getValue());
                	this.controlador.crearNuevoMedicamento(medicamentoNuevo);
                	this.controlador.consultarMedicamentos();
                	populateForm(null);
                }else {
                	
                }
            });

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(codigo, nombre, fabricante, principioa, fechav, stock, actions);
            return container;
        }

    private void populateForm(Medicamentos value) {
    	this.medicamentoNuevo = value;
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
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);
        grid.asSingleSelect().addValueChangeListener(event -> {
        	if(event.getValue() != null) {
        		populateForm(event.getValue());
        	}else {
        		populateForm(null);
        	}
        });
        return grid;
    }
    
	@Override
	public void refrescarGridMedicamentos(List<Medicamentos> medicamento) {
		Collection<Medicamentos> items = medicamento;
		grid.setItems(items);
		this.medicamentos = medicamento;
	}

	@Override
	public void mostrarMensajeCreacion(boolean exito) {
	      String mesajeMotrar = "Registro creado con exito";
	      if(!exito) {
	    	  mesajeMotrar = "Registro no pudo ser creado";
	      }
	      
	      Notification.show(mesajeMotrar);		
	}

	@Override
	public void mostrarMensajeEliminar(boolean exito) {
	      String mesajeMotrar = "Registro eliminado con exito";
	      if(!exito) {
	    	  mesajeMotrar = "Registro no pudo ser eliminado";
	      }
	      
	      Notification.show(mesajeMotrar);
	}
    
}