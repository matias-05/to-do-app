package app.todo.views.papelera;

//#region imports
import org.springframework.data.domain.Pageable;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import app.todo.data.Task;
import app.todo.services.TaskService;
//#endregion

@PageTitle("Papelera")
@Route("papelera")
@Menu(order = 3, icon = LineAwesomeIconUrl.RECYCLE_SOLID)
public class PapeleraView extends Composite<VerticalLayout> {

    private final TaskService taskService;
    private Task selectedTask;
    private final Grid<Task> taskGrid;
    
    public PapeleraView(TaskService taskService) {

        HorizontalLayout layoutRow = new HorizontalLayout();
        this.taskService = taskService;

        //Configuracion de grid
        taskGrid = new Grid<>(Task.class, false);
        taskGrid.addColumn(Task::getDescription).setHeader("Descripción").setAutoWidth(true);
        taskGrid.addColumn(Task::getDueDate).setHeader("Vencimiento").setAutoWidth(true);
        taskGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taskGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedTask = event.getValue();
        });

        //Configuracion de boton elminiar
        Button btnEliminar = new Button();
        btnEliminar.setText("Eliminar definitivamente");
        btnEliminar.setWidth("min-content");
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        btnEliminar.addClickListener(e -> {
            if (selectedTask != null) {
                Dialog confirmDialog = new Dialog();
                confirmDialog.add(new Span("¿Estás seguro de que deseas eliminar esta tarea definitivamente?"));
                Button confirmButton = new Button("Eliminar", a -> {
                    taskService.deleteTask(selectedTask);
                    refreshGrid();
                    selectedTask = null;
                    taskGrid.deselectAll();
                    confirmDialog.close();
                });
                Button cancelButton = new Button("Cancelar", a -> confirmDialog.close());
                confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
                cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                HorizontalLayout buttons = new HorizontalLayout(confirmButton, cancelButton);
                confirmDialog.add(buttons);
                confirmDialog.open();
            } else {
                Notification.show("Seleccione una tarea de la lista", 2000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        //Configuracion de boton recuperar
        Button btnRecuperar = new Button();
        btnRecuperar.setText("Recuperar tarea");
        btnRecuperar.setWidth("min-content");
        btnRecuperar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnRecuperar.addClickListener(e -> {
            if (selectedTask != null) {
                selectedTask.setInTrash(false);
                taskService.updateTask(selectedTask);
                refreshGrid();
                selectedTask = null;
                taskGrid.deselectAll();
            } else {
                Notification.show("Seleccione una tarea de la lista", 2000, Notification.Position.MIDDLE)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        //Configuracion de frontend
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        getContent().add(layoutRow);
        layoutRow.add(btnRecuperar);
        layoutRow.add(btnEliminar);
        getContent().add(taskGrid);
        refreshGrid();
    }

    //Metodos
    private void refreshGrid() {
        taskGrid.setItems(
        taskService.list(Pageable.unpaged())
            .stream()
            .filter(task -> task.isInTrash())
            .toList()
    );
    }
}
