package app.todo.data;
//#region imports
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
//#endregion

@Entity
@Table(name = "person")
public class Person {

    public static final int NAME_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private Long id;

    public Long getId() {
        return id;
    }   

    @Column(name = "name", nullable = false, length = NAME_MAX_LENGTH)
    @Size(max = NAME_MAX_LENGTH)
    private String name;

    @Column(name = "last_name", nullable = false, length = NAME_MAX_LENGTH)
    @Size(max = NAME_MAX_LENGTH)
    private String lastName;

    @Column(name = "dni", nullable = false, length = 9)
    @Size(max = 9)
    private String dni;

    @Column(name = "tareasAsignadas")
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    public List<Task> getTasks() {
        return tasks;
    }   
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    @Override
    public String toString() {
        return name + " " + lastName + " (" + dni + ")";
    }
    

}
