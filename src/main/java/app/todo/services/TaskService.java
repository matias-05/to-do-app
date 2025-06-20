package app.todo.services;
//#region imports
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import app.todo.data.Task;
import app.todo.data.TaskRepository;
//#endregion

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TaskService {

    private final TaskRepository taskRepository;

    //Constructor
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //Metodo para crear una tarea y guardarla en la base de datos
    public void createTask(String description, @Nullable LocalDate dueDate) {
        if ("fail".equals(description)) {
            throw new RuntimeException("This is for testing the error handler");
        }
        var task = new Task();
        task.setDescription(description);
        task.setCreationDate(java.time.Instant.now(Clock.systemUTC()));
        task.setDueDate(dueDate);
        taskRepository.saveAndFlush(task);
    }

    //Metodo para actualizar una tarea en la base de datos
    public void updateTask(Task task){
        taskRepository.saveAndFlush(task);
    }

    //Metodo para ver lista de tareas guardadas en la base de datos
    public List<Task> list(Pageable pageable) {
        return taskRepository.findAllBy(pageable).toList();
    }

    //Metodo para borrar tarea de la base de datos
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

}