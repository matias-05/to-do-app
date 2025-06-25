package app.todo.views.personas;

//#region imports
import org.springframework.data.domain.Pageable;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import app.todo.data.Person;
import app.todo.services.PersonService;
//#endregion

@PageTitle("Personas")
@Route("personas")
@Menu(order = 1, icon = LineAwesomeIconUrl.USER)
public class PersonasView extends Composite<VerticalLayout> {

    //Declaración de componentes
    private final PersonService personService;
    final Grid<Person> personGrid;
    private Person selectedPerson;

    public PersonasView(PersonService personService) {

        this.personService = personService;
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout content = new VerticalLayout();

        //Configuración de lastName
        TextField lastName = new TextField();
        lastName.setPlaceholder("Escribe el apellido aquí");
        lastName.setAriaLabel("Apellido");
        lastName.setMaxLength(Person.NAME_MAX_LENGTH);
        lastName.setMinWidth("20em");
        lastName.setLabel("Apellido:");
        lastName.setWidth("190px");
        lastName.setHeight("50px");

        // Configuración de name
        TextField name = new TextField();
        name.setPlaceholder("Escribe el nombre aquí");
        name.setAriaLabel("Nombre");
        name.setMaxLength(Person.NAME_MAX_LENGTH);
        name.setMinWidth("20em");
        name.setLabel("Nombre:");
        name.setWidth("190px");
        name.setHeight("50px");

        // Configuración de dni
        TextField dni = new TextField();
        dni.setPlaceholder("Escribe el dni aquí");
        dni.setAriaLabel("Dni");
        dni.setMaxLength(9);
        dni.setMinWidth("20em");
        dni.setLabel("Dni:");
        dni.setWidth("190px");
        dni.setHeight("50px");

        // Configuración del grid
        personGrid = new Grid<>(Person.class, false);
        personGrid.addColumn(Person::getDni).setHeader("Dni").setAutoWidth(true);
        personGrid.addColumn(Person::getName).setHeader("Nombre").setAutoWidth(true);
        personGrid.addColumn(Person::getLastName).setHeader("Apellido").setAutoWidth(true);
        personGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        personGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedPerson = event.getValue();
        });
        
        //  Configuración del botón de crear persona
        Button createBtn = new Button("Crear Persona", VaadinIcon.PLUS_CIRCLE_O.create(), event -> {
            personService.createPerson(name.getValue(), lastName.getValue(), dni.getValue());
            name.clear();
            lastName.clear();   
            dni.clear();
            refreshGrid();
        });
        createBtn.setWidth("170px");
        createBtn.setHeight("30px");
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //  Configuración del botón de eliminar persona
        Button deleteBtn = new Button("Borrar Persona", VaadinIcon.TRASH.create(), event -> {
            if (selectedPerson != null) {
                Dialog confirmDialog = new Dialog();
                confirmDialog.add(new Span("¿Estás seguro de que borrar esta persona?"));
                Button confirmButton = new Button("Borrar", e -> {
                    personService.deletePerson(selectedPerson);
                    refreshGrid();
                    confirmDialog.close();
                });
                Button cancelButton = new Button("Cancelar", e -> confirmDialog.close());
                confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
                confirmDialog.add(buttons);
                confirmDialog.open();
            }
        });
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteBtn.setWidth("170px");
        deleteBtn.setHeight("30px");


        // Configuración del frontend
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.START);
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, name);
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, lastName);
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, dni);
        getContent().add(layoutRow);
        layoutRow.add(name, lastName, dni);
        content.add(createBtn, deleteBtn, personGrid);
        getContent().add(content);
        refreshGrid();
        
    }

    //Metodos
    private void refreshGrid() {
        personGrid.setItems(
            personService.list(Pageable.unpaged())
                .stream()
                .toList()
        );
    }
}