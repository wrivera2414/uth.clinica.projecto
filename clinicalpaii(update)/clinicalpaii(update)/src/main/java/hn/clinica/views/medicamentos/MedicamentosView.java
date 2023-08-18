//----->   MedicamentosView.java
package hn.clinica.views.medicamentos;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import hn.clinica.data.controller.MedicamentosInteractor;
import hn.clinica.data.controller.MedicamentosInteractorImpl;
import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.Consulta;
import hn.clinica.data.entity.ConsultaReporte;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.data.entity.MedicamentosReporte;
import hn.clinica.data.service.ReportGenerator;
import hn.clinica.views.MainLayout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //medicamentos = new ArrayList<>();        
        codigo.addClassName(Margin.Right.LARGE);
        nombre.addClassName(Margin.Right.LARGE);
        fabricante.addClassName(Margin.Right.LARGE);
        principioa.addClassName(Margin.Right.LARGE);
        fechav.addClassName(Margin.Right.LARGE);
        stock.addClassName(Margin.Right.LARGE);
        
        this.controlador = new MedicamentosInteractorImpl(this);
    	
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), Filters(), createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
        
        GridContextMenu<Medicamentos> menu = grid.addContextMenu();
     	menu.addItem("Generar Reporte", event -> {
     		if(this.medicamentos.isEmpty()) {
         		Notification.show("No hay datos para generar el reporte");
         	}else {
         		Notification.show("Generando reporte PDF...");
         		generarReporteConsulta();
         	}
     	});
    }

    
    private void generarReporteConsulta() {
		ReportGenerator generador = new ReportGenerator(); 
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("LOGO_DIR", "loguito.png");
		parametros.put("LOGO_BAR", "loguito.png");
		MedicamentosReporte datasource = new MedicamentosReporte();
		datasource.setData(medicamentos);
		boolean generado = generador.generarReportePDF("Reporte_Medicamentos", parametros, datasource);
		if(generado) {
	    	String ubicacion = generador.getUbicacion();
	    	Anchor url = new Anchor(ubicacion, "Abrir reporte PDF");
			url.setTarget("_Blank"); 
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