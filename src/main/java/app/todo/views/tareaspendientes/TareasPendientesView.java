package app.todo.views.tareaspendientes;
//#region imports
import org.springframework.data.domain.Pageable;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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

import app.todo.data.Task;
import app.todo.services.TaskService;
//#endregion

@PageTitle("Tareas Pendientes")
@Route("tareas-pendientes")
@Menu(order = 1, icon = LineAwesomeIconUrl.CLOCK)
public class TareasPendientesView extends Composite<VerticalLayout> {

    //Declaración de componentes
    private final TaskService taskService;
    final TextField description;
    final DatePicker dueDate;
    final Button createBtn;
    final Grid<Task> taskGrid;

    public TareasPendientesView(TaskService taskService) {

        HorizontalLayout layoutRow = new HorizontalLayout();
        this.taskService = taskService;
        
        // Configuración de descripcion
        description = new TextField();
        description.setPlaceholder("Escribe tu tarea aquí");
        description.setAriaLabel("Task description");
        description.setMaxLength(Task.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");
        description.setLabel("Tarea por hacer");
        description.setWidth("190px");
        description.setHeight("50px");

        // Configuración de fecha de vencimiento
        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");
        dueDate.setLabel("Vencimiento de la tarea");
        dueDate.setWidth("190px");
        dueDate.setHeight("50px");

        // Configuración del botón de crear tarea
        createBtn = new Button("Create", event -> {createTask(); refreshGrid();});
        createBtn.setText("Agregar");
        createBtn.setWidth("20px");
        createBtn.setHeight("30px");
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Configuración del grid de tareas
        taskGrid = new Grid<>(Task.class, false);
        
        taskGrid.addComponentColumn(task -> {
            Button doneBtn = new Button(VaadinIcon.CHECK_CIRCLE.create(), event -> {
                Dialog confirmDialog = new Dialog();
                confirmDialog.add(new Span("¿Estás seguro de que deseas marcar esta tarea como hecha?"));

                Button confirmButton = new Button("Aceptar", e -> {
                    task.setDone(true);
                    taskService.updateTask(task);
                    refreshGrid();
                    confirmDialog.close();
                });
                Button cancelButton = new Button("Cancelar", e -> confirmDialog.close());
                confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
                confirmDialog.add(buttons);
                confirmDialog.open();
            });
            doneBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return doneBtn;
        }).setHeader("Marcar como hecho").setWidth("200px").setFlexGrow(0);
        taskGrid.addColumn(Task::getDescription).setHeader("Descripción").setAutoWidth(true);
        taskGrid.addColumn(Task::getDueDate).setHeader("Vencimiento").setWidth("50px");
        taskGrid.addComponentColumn(task -> {
            Button deleteBtn = new Button(VaadinIcon.CLOSE.create(), event -> {
                Dialog confirmDialog = new Dialog();
                confirmDialog.add(new Span("¿Estás seguro de que deseas enviar esta tarea a la papelera?"));

                Button confirmButton = new Button("Borrar", e -> {
                    task.setInTrash(true);
                    taskService.updateTask(task);
                    refreshGrid();
                    confirmDialog.close();
                });
                Button cancelButton = new Button("Cancelar", e -> confirmDialog.close());
                confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
                confirmDialog.add(buttons);
                confirmDialog.open();
            });
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            return deleteBtn;
        }).setHeader("Borrar").setAutoWidth(true).setFlexGrow(0).setTextAlign(ColumnTextAlign.START);
        
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.START);
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");
        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, createBtn);
        getContent().add(layoutRow);
        layoutRow.add(description);
        layoutRow.add(dueDate);
        layoutRow.add(createBtn);
        getContent().add(layoutRow, taskGrid);
        layoutRow.add(description, dueDate, createBtn);    
        refreshGrid();
    }
    
    //Metodos
    public void createTask() {
        taskService.createTask(description.getValue(), dueDate.getValue());
        taskGrid.getDataProvider().refreshAll();
        description.clear();
        dueDate.clear();
        
    }
    private void refreshGrid() {
        taskGrid.setItems(
            taskService.list(Pageable.unpaged())
                .stream()
                .filter(task -> !task.isDone() && !task.isInTrash())
                .toList()
        );
    }    
}
