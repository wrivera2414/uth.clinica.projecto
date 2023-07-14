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
    	cita = new ArrayList<>();
        this.controlador = new CitasInteractorImpl(this);
    //public CitasView(CitasService citasService) {
        //this.citasService = citasService;
        addClassNames("citas-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("idcita").setAutoWidth(true);
        grid.addColumn("fecha").setAutoWidth(true);
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
                UI.getCurrent().navigate(String.format(CITAS_EDIT_ROUTE_TEMPLATE, event.getValue().getIdcita()));
            } else {
                clearForm();
                UI.getCurrent().navigate(CitasView.class);
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
        });

        save.addClickListener(e -> {
            try {
                if (this.citas == null) {
                    this.citas = new Citas();
                }
                
                //citasService.update(this.citas);
                clearForm();
                refreshGrid();
                Notification.show("Registro actualizado con exito");
                UI.getCurrent().navigate(CitasView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al actualizar los datos. Alguien más ha actualizado el registro mientras estabas haciendo cambios.");
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
        idcita = new TextField("Numero de Cita");
        fecha = new TextField("Fecha");
        paciente = new TextField("Paciente");
        paciente.setPrefixComponent(VaadinIcon.USER.create());
        direccion = new TextField("Direccion");
        direccion.setPrefixComponent(VaadinIcon.LOCATION_ARROW.create());
        telefono = new TextField("Telefono");
        telefono.setPrefixComponent(VaadinIcon.PHONE_LANDLINE.create());
        detalle = new TextArea("Detalle");
        detalle.setLabel("Comentario");
        detalle.setMaxLength(140);
        detalle.setValueChangeMode(ValueChangeMode.EAGER);
        detalle.addValueChangeListener(e -> {
            e.getSource()
                    .setHelperText(e.getValue().length() + "/" + (140));
        });
        detalle.setValue("Detalle de la cita");
        add(detalle);
        formLayout.add(idcita, fecha, paciente, direccion, telefono, detalle);

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
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Citas value) {
        if(value == null){
            this.idcita.setValue("");
        	this.fecha.setValue("");
        	this.paciente.setValue("");
        	this.direccion.setValue("");
        	this.telefono.setValue("");
        	this.detalle.setValue("");
        	} 
        else {
        	this.idcita.setValue(value.getIdcita());
        	this.fecha.setValue(value.getFecha());
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
}