package app.todo.views.tareasrealizadas;

//#region imports
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import app.todo.data.Task;
import app.todo.services.TaskService;
import org.springframework.data.domain.Pageable;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
//#endregion

@PageTitle("Tareas Realizadas")
@Route("tareas-realizadas")
@Menu(order = 2, icon = LineAwesomeIconUrl.CHECK_SOLID)
public class TareasRealizadasView extends Composite<VerticalLayout> {

    private final TaskService taskService;
    final Grid<Task> taskGrid;
    private Task selectedTask;

    public TareasRealizadasView(TaskService taskService) {
        HorizontalLayout layoutRow = new HorizontalLayout();
        this.taskService = taskService;

        // Configuración de grid
        taskGrid = new Grid<>(Task.class, false);
        taskGrid.addColumn(Task::getDescription).setHeader("Descripción").setAutoWidth(true);
        taskGrid.addColumn(Task::getDueDate).setHeader("Vencimiento").setAutoWidth(true);
        taskGrid.addColumn(task -> task.getPerson() != null ? task.getPerson().getName() + " " + task.getPerson().getLastName() : "").setHeader("Realizada por").setAutoWidth(true);
        taskGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taskGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedTask = event.getValue();
        });

        // Configuración de boton recuperar
        Button btnRecuperar = new Button();
        btnRecuperar.setText("Regresar a pendientes");
        btnRecuperar.setWidth("min-content");
        btnRecuperar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnRecuperar.addClickListener(e -> {
            if (selectedTask != null) {
                selectedTask.setDone(false);
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

        // Configuración de frontend
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("100%");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        getContent().add(layoutRow);
        layoutRow.add(btnRecuperar);
        getContent().add(taskGrid);
        refreshGrid();
    }

    //Métodos
    private void refreshGrid() {
        taskGrid.setItems(
        taskService.list(Pageable.unpaged())
            .stream()
            .filter(task -> task.isDone())
            .toList()
    );
    }
}
