package app.todo.views.papelera;

import org.springframework.data.domain.Pageable;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

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

@PageTitle("Papelera")
@Route("papelera")
@Menu(order = 3, icon = LineAwesomeIconUrl.RECYCLE_SOLID)
public class PapeleraView extends Composite<VerticalLayout> {
    private final TaskService taskService;
    final Grid<Task> taskGrid;
    private Task selectedTask;
    
    public PapeleraView(TaskService taskService) {
        this.taskService = taskService;
        taskGrid = new Grid<>(Task.class, false);
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button btnRecuperar = new Button();
        Button btnEliminar = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        btnRecuperar.setText("Recuperar tarea");
        btnRecuperar.setWidth("min-content");
        btnRecuperar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnEliminar.setText("Eliminar definitivamente");
        btnEliminar.setWidth("min-content");
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        getContent().add(layoutRow);
        layoutRow.add(btnRecuperar);
        layoutRow.add(btnEliminar);
        taskGrid.addColumn(Task::getDescription).setHeader("Descripción").setAutoWidth(true);
        taskGrid.addColumn(Task::getDueDate).setHeader("Vencimiento").setAutoWidth(true);
        taskGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        taskGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedTask = event.getValue();
        });
        btnRecuperar.addClickListener(e -> {
            if (selectedTask != null) {
                selectedTask.setInTrash(false);
                taskService.updateTask(selectedTask);
                refreshGrid();
                selectedTask = null;
                taskGrid.deselectAll();
            }
        });
        btnEliminar.addClickListener(e -> {
            if (selectedTask != null) {
                taskService.deleteTask(selectedTask);
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
            .filter(task -> task.isInTrash())
            .toList()
    );
    }
}
