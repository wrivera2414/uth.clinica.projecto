package hn.clinica.views.citas;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.Collection;
import hn.clinica.data.controller.CitasInteractor;
import hn.clinica.data.controller.CitasInteractorImpl;
import hn.clinica.data.entity.Citas;
import hn.clinica.views.MainLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Citas")
@Route(value = "citas/:citasID?/:action?(edit)", layout = MainLayout.class)
public class CitasView extends Div implements BeforeEnterObserver,CitasViewModel {

    private final String CITAS_ID = "citasID";
    private final String CITAS_EDIT_ROUTE_TEMPLATE = "citas/%s/edit";
    private final Grid<Citas> grid = new Grid<>(Citas.class, false);

    private TextField idcita;
    private TextField fecha;
    private TextField identidad;
    private TextField paciente;
    private TextField direccion;
    private TextField telefono;
    private TextArea detalle;
    private List<Citas> cita;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

   // private final BeanValidationBinder<Citas> binder;

    private Citas citas;
    private CitasInteractor controlador;

    //private final CitasService citasService;

    public CitasView() {
    	addClassNames("citas-view");
    	cita = new ArrayList<>();
        this.controlador = new CitasInteractorImpl(this);
    //public CitasView(CitasService citasService) {
        //this.citasService = citasService;
        
        
        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("idcita").setAutoWidth(true);
        grid.addColumn("fecha").setAutoWidth(true);
        grid.addColumn("identidad").setAutoWidth(true);
        grid.addColumn("paciente").setAutoWidth(true);
        grid.addColumn("direccion").setAutoWidth(true);
        grid.addColumn("telefono").setAutoWidth(true);
        grid.addColumn("detalle").setAutoWidth(true);
        /*grid.setItems(query -> citasService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());*/
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
        	clearForm();
            if (event.getValue() != null) {
                identidad.setReadOnly(true);

                UI.getCurrent().navigate(String.format(CITAS_EDIT_ROUTE_TEMPLATE, event.getValue().getIdcita()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CitasView.class);
                identidad.setReadOnly(false);

            }
        });

        // Configure Form
       // binder = new BeanValidationBinder<>(Citas.class);

        // Bind fields. This is where you'd define e.g. validation rules
        //binder.forField(idcita).withConverter(new StringToIntegerConverter("Unicamente son permitidos Numeros")).bind("idcita");

        //binder.bindInstanceFields(this);
        
        this.controlador.consultarCitas();

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
            identidad.setReadOnly(false);

        });

        save.addClickListener(e -> {
            try {
            	String MensajeExito = "Registro Guardado!";
                if (this.citas == null) {
                    //CEANDO REGISTRO 
                	this.citas = new Citas();
                	this.citas.setIdcita(this.idcita.getValue());
                	this.citas.setFecha(this.fecha.getValue());
                	this.citas.setIdentidad(this.identidad.getValue());
                	this.citas.setPaciente(this.paciente.getValue());
                	this.citas.setTelefono(this.telefono.getValue());
                	this.citas.setDireccion(this.direccion.getValue());
                	this.citas.setDetalle(this.detalle.getValue());
                	this.controlador.crearNuevaCitas(citas);  
                    this.controlador.consultarCitas();

                }else {
                    //MODIFICANDO REGISTRO
                }
                clearForm();
                refreshGrid();
                UI.getCurrent().navigate(CitasView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show("Error al actualizar los datos. revisa tu conexion ");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
                
            } 
            
        });
        
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> citasId = event.getRouteParameters().get(CITAS_ID);
      boolean encontrado = false; 
        if (citasId.isPresent()) {
           for (Citas e: this.cita) {
        	   if (e.getIdcita().equals(citasId.get()) ) {
        		   populateForm(e);
        		   encontrado = true ;
        		   break;
        	   }
           }
            if (!encontrado) {
            	Notification.show(String.format("la Cita con ID = %s", citasId.get()+ "no fue encontrado"),
                        3000,  Notification.Position.BOTTOM_START);
                  // when a row is selected but the data is no longer available,
                  // refresh grid
                  refreshGrid();
                  event.forwardTo(CitasView.class);
            } 
                
            
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        idcita = new TextField("NumerodeCita");
       // idcita.setPrefixComponent(VaadinIcon.USER_CARD.create());
        fecha = new TextField("Fecha");        
        //fecha = new DateTimePicker("Fecha");
        fecha .setPrefixComponent(VaadinIcon.DATE_INPUT.create());
        fecha.setHelperText("Horario de 8:00AM a 4:00PM");
        fecha.setLabel("Fecha y hora de la cita");
        /*fecha.setStep(Duration.ofMinutes(30));
        fecha.setHelperText("Elija en rango de 7 dias de 8AM/6PM");
        fecha.setAutoOpen(true);
        fecha.setMin(LocalDateTime.now());
        fecha.setMax(LocalDateTime.now().plusDays(7));
        fecha.setValue(LocalDateTime.now().plusDays(0));
        fecha.addValueChangeListener(event -> {
            LocalDateTime value = event.getValue();
            String errorMessage = null;
            if (value != null) {
                if (value.compareTo(fecha.getMin()) < 8) {
                    errorMessage = "Demaciado Temprano, Elija Otra Fecha y hora";
                } else if (value.compareTo(fecha.getMax()) > 16) {
                    errorMessage = "Demaciado Tarde, Elija Otra Fecha y hora";
                }
            }
            fecha.setErrorMessage(errorMessage);
        });
        add(fecha);*/
       
        identidad = new TextField("Identidad");
        identidad.setPlaceholder("Busqueda Paciente");
        identidad.setPrefixComponent(VaadinIcon.BACKSPACE.create());
        paciente = new TextField("Paciente");
       // paciente.setReadOnly(true);
        paciente.setPrefixComponent(VaadinIcon.USER.create());
        direccion = new TextField("Direccion");
        //direccion.setReadOnly(true);
        direccion.setPrefixComponent(VaadinIcon.LOCATION_ARROW.create());
        telefono = new TextField("Telefono");
        //telefono.setReadOnly(true);
        telefono.setPrefixComponent(VaadinIcon.PHONE_LANDLINE.create());
        detalle = new TextArea("Detalle");
        detalle.setPlaceholder("Ingrese el detalle de la cita");
        detalle.setPrefixComponent(VaadinIcon.INFO_CIRCLE.create());
        detalle.setLabel("Comentario");
        detalle.setMaxLength(140);
        detalle.setValueChangeMode(ValueChangeMode.EAGER);
        detalle.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + (140));
        });
        //detalle.setValue("Detalle de la cita");
        formLayout.add(idcita, fecha, identidad, paciente, direccion, telefono, detalle);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv); 

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
    	this.controlador.consultarCitas();
    	grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Citas value) {
    	this.citas = value;
        if(value == null){	
            this.idcita.setValue("");
        	this.fecha.setValue("");
        	this.identidad.setValue("");
        	this.paciente.setValue("");
        	this.direccion.setValue("");
        	this.telefono.setValue("");
        	this.detalle.setValue("");
        	} 
        else {
        	//DateTimePicker fechastr = ;
        	this.idcita.setValue(value.getIdcita());
        	this.fecha.setValue(value.getFecha());
        	this.identidad.setValue(value.getIdentidad());
        	this.paciente.setValue(value.getPaciente());
        	this.direccion.setValue(value.getDireccion());
        	this.telefono.setValue(value.getTelefono());
        	this.detalle.setValue(value.getDetalle());
        }
    }
   @Override
	public void refrescarGridCitas(List<Citas> cita) {
		// TODO Auto-generated method stub
	//Este Metodo refresca el Grid
		
		Collection<Citas> items = cita;
		grid.setItems(items);
		this.cita = cita;
		
	
	}

@Override
public void mostrarMensajeCreacion(boolean exito) {
      String mesajeMotrar = "Registro creado con exito";
      if(!exito) {
    	  mesajeMotrar = "Registro no pudo ser creado";
      }
      
      Notification.show(mesajeMotrar);

}
}