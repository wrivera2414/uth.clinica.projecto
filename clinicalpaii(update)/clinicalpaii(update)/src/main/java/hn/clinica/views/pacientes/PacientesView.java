 package hn.clinica.views.pacientes;

import com.vaadin.flow.component.UI;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import hn.clinica.data.controller.PacientesInteractor;
import hn.clinica.data.controller.PacientesInteractorImpl;
import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.views.MainLayout;
import net.sf.jasperreports.compilers.JavaScriptCallableThisDecorator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.swing.JComboBox;

import org.hibernate.jdbc.BatchedTooManyRowsAffectedException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.ArrayList;
import com.vaadin.flow.component.select.Select;


@PageTitle("Pacientes")
@Route(value = "pacientes/:pacientesID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class PacientesView extends Div implements BeforeEnterObserver, PacientesViewModel {

	private static final String PACIENTES_ID = "pacientesID";
	private final String PACIENTES_EDIT_ROUTE_TEMPLATE = "pacientes/%s/edit";
    private final Grid<Pacientes> grid = new Grid<>(Pacientes.class, false);
    private TextField nombre;
    private TextField identidad;
    private TextField telefono;
    private TextField edad;
    private TextField peso;
    private TextField altura;
    private TextField sangre;
    private List<Pacientes> pacientes;


    private final Button cancel = new Button("Limpiar");
    private final Button save = new Button("Guardar");
    private final Button btnEliminar = new Button("Eliminar");

    private Pacientes paciente;
    private PacientesInteractor controlador;

    @SuppressWarnings("unlikely-arg-type")
    //CONTRUCTOR VIEW PACIENTES
	public PacientesView() {
    	
    	
        addClassNames("pacientes-view");
        pacientes = new ArrayList<>();
        this.controlador = new PacientesInteractorImpl(this);

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("identidad").setAutoWidth(true);
        grid.addColumn("telefono").setAutoWidth(true);
        grid.addColumn("edad").setAutoWidth(true);
        grid.addColumn("sangre").setAutoWidth(true);
        grid.addColumn("peso").setAutoWidth(true);
        grid.addColumn("altura").setAutoWidth(true);  
        
        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(PACIENTES_EDIT_ROUTE_TEMPLATE, event.getValue().getIdentidad()));
            } else {
                clearForm();
                UI.getCurrent().navigate(PacientesView.class);
            }
        });
        
        //mando a traer los pacientes del repositorio
        this.controlador.consultarPacientes();

        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        GridContextMenu<Pacientes> menu = grid.addContextMenu();
        menu.addItem("Eliminar", event -> {
         	ConfirmDialog dialog = new ConfirmDialog();
        	dialog.setHeader("Eliminar Paciente de "+event.getItem().get().getIdentidad());
        	dialog.setText("Confirma que deseas eliminar El paciente!");
        	dialog.setCancelable(true);
        	dialog.setCancelText("Cancelar");
        	dialog.setCancelButtonTheme("cancel primary");
        	dialog.addCancelListener(event2 -> {
             refreshGrid();	
        	});
        	dialog.setConfirmText("Eliminar");
        	dialog.setConfirmButtonTheme("error primary");
        	dialog.addConfirmListener(event3 -> {
        	this.controlador.eliminarPaciente(event.getItem().get().getIdentidad());
        	});
        	dialog.open();
        	
        });
        

        btnEliminar.addClickListener(e -> {
        	
        	try {
        	 if (this.paciente == null)
             {    
             	String mensajeerror= "CAMPO VACIO";
             		Notification.show(mensajeerror);
             	} else 
             	{
             		this.controlador.eliminarPaciente(this.paciente.getIdentidad());
             	
				
             	}
        	 
        	} catch (ObjectOptimisticLockingFailureException Exception) {
        		  Notification n = Notification.show(
                          "Error al Eliminar informacion por favor revise su conexion o intente nuevamente");
                  n.setPosition(Position.MIDDLE);
                  n.addThemeVariants(NotificationVariant.LUMO_ERROR);			}
        });
        
        

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });
      
        
     
        save.addClickListener(e -> {
            try {
            	
                if (this.paciente == null)
                {    
                	//CREANDO REGISTROS PANTALLA PACIENTES
                    	this.paciente = new Pacientes();
                    	this.paciente.setIdentidad(this.identidad.getValue());
                    	this.paciente.setNombre(this.nombre.getValue());
                    	this.paciente.setTelefono(this.telefono.getValue());
                    	this.paciente.setSangre(this.sangre.getValue());
                    	this.paciente.setEdad(this.edad.getValue());
                    	this.paciente.setPeso(this.peso.getValue());
                    	this.paciente.setAltura(this.altura.getValue());
                    	this.controlador.crearPacientes(paciente);

                	} else 
                	{
                		this.paciente.setIdentidad(this.identidad.getValue());
                		this.paciente.setNombre(this.nombre.getValue());
                        this.paciente.setTelefono(this.telefono.getValue());
                  	    this.paciente.setSangre(this.sangre.getValue().toString());	 
                        this.paciente.setEdad(this.edad.getValue());
                        this.paciente.setPeso(this.peso.getValue());
                        this.paciente.setAltura(this.altura.getValue());
                      	this.controlador.modificarPacientes(paciente);

					}
     
          
                refreshGrid();
                clearForm();
                UI.getCurrent().navigate(PacientesView.class);

            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error al almacenar informacion por favor revise su conexion o intente nuevamente");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        });
        
      
    }
    

    @Override
    
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> pacientesId = event.getRouteParameters().get(PACIENTES_ID);
        boolean encontrado = false;
       if (pacientesId.isPresent()){
        	for (Pacientes e: this.pacientes){
        		if(e.getIdentidad().equals(pacientesId.get()))
        		{
                    populateForm(e);
                    encontrado = true;
                    break;
        		}	
        	}
        		if(!encontrado) 
        		{
        		 Notification.show(String.format("El empleado con identidad= %s", pacientesId.get() + "No fue encontrado"),
                             3000, Notification.Position.BOTTOM_START);
                     refreshGrid();
                     event.forwardTo(PacientesView.class); 
        			
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
        nombre = new TextField("Nombre");
        nombre.setClearButtonVisible(true);
        nombre.setPrefixComponent(VaadinIcon.USER_CARD.create());
        
        identidad = new TextField("Identidad");
        identidad.setSuffixComponent(new Span("DNI"));
        identidad.setPrefixComponent(VaadinIcon.USER_CHECK.create());
        telefono = new TextField("Telefono");
        telefono.setPrefixComponent(new Span("+504"));
        edad = new TextField("Edad");
        edad.setSuffixComponent(new Span("Años"));
        sangre = new TextField("Sangre");
        
      
        
        peso = new TextField("Peso");
        peso.setSuffixComponent(new Span("Lbs"));

        
        altura = new TextField("Altura");
        altura.setSuffixComponent(new Span("Cm"));
    

        //Agregar Componentes al Layout
        formLayout.add(nombre, identidad,telefono,sangre, edad, peso, altura);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

	private void createButtonLayout(Div editorLayoutDiv) {
		
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_ERROR);
        buttonLayout.add(save, cancel, btnEliminar);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
    	this.controlador.consultarPacientes();
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Pacientes value) {
    	this.paciente = value;
    	if(value == null) 
    	{	
    		this.nombre.setValue("");	
		    this.identidad.setValue("");	
		    this.telefono.setValue("");	
		    this.edad.setValue("");	
		    this.sangre.setValue("");	
		    this.peso.setValue("");	
		    this.altura.setValue("");
    	}
    	else 
    	{
    		this.nombre.setValue(value.getNombre());	
    	    this.identidad.setValue(value.getIdentidad());	
    	    this.telefono.setValue(value.getTelefono());	
    	    this.edad.setValue(value.getEdad());
    	    this.sangre.setValue(value.getSangre());
    	    this.peso.setValue(value.getPeso());	
    	    this.altura.setValue(value.getAltura());	
    	
    	} 	
    	
   }

	@Override
	public void refrescarGridPacientes(List<Pacientes> pacientes) {
		//Este Metodo refresca el Grid
		Collection<Pacientes> items = pacientes;
		grid.setItems(items);
		this.pacientes = pacientes;
		
	}


	@Override
	public void mostrarMensajeCreacion(boolean Exito) {
		
        String MensajeExito = "Registro Creado Con Exito!";
        
		if(!Exito) 
		{
			MensajeExito = "Empleado No Pudo Ser Creado";
			
			}
	
        Notification.show(MensajeExito);

		
	}


	@Override
	public void mostrarMensajeActualizacionPacientes(boolean exito) {
		String mesajeMotrar = "Registro actualizado con exito";
	    if(!exito) {
	  	  mesajeMotrar = "Registro no pudo ser actualizado";
	    }
	    
	    Notification.show(mesajeMotrar);
		
	}


	@Override
	public void mostrarMensajeEliminacionPacientes(boolean exito) {
		String EliminarPaciente = "Registro Eliminado con exito";
		refreshGrid();

	    if(!exito) {
	    	EliminarPaciente = "Registro no pudo ser Eliminado";
	    }
	    
	    Notification.show(EliminarPaciente);
				
	}
}
 