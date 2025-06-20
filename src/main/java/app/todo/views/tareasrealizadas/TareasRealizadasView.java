package app.todo.views.tareasrealizadas;
//#region imports
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
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
        this.taskService = taskService;
        taskGrid = new Grid<>(Task.class, false);
        HorizontalLayout layoutRow = new HorizontalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        Button btnRecuperar = new Button();
        btnRecuperar.setText("Recuperar tarea");
        btnRecuperar.setWidth("min-content");
        btnRecuperar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("80px");
        layoutRow2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        getContent().add(layoutRow);
        getContent().add(layoutRow2);
        layoutRow.add(btnRecuperar);
        taskGrid.addColumn(Task::getDescription).setHeader("Descripción").setAutoWidth(true);
        taskGrid.addColumn(Task::getDueDate).setHeader("Vencimiento").setAutoWidth(true);
        taskGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taskGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedTask = event.getValue();
        });
        btnRecuperar.addClickListener(e -> {
            if (selectedTask != null) {
                selectedTask.setDone(false);
                selectedTask.setInTrash(false);
                taskService.updateTask(selectedTask);
                refreshGrid();
                selectedTask = null;
                taskGrid.deselectAll();
            }
        });
        getContent().add(taskGrid);
        refreshGrid();
    }

    private void refreshGrid() {
        taskGrid.setItems(
        taskService.list(Pageable.unpaged())
            .stream()
            .filter(task -> task.isDone())
            .toList()
    );
    }
}
