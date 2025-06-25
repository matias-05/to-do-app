package app.todo.data;
//#region imports
import java.time.Instant;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
//#endregion

@Entity
@Table(name = "task")
public class Task extends AbstractEntity<Long> {

    public static final int DESCRIPTION_MAX_LENGTH = 255;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "description", nullable = false, length = DESCRIPTION_MAX_LENGTH)
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    @Column(name = "due_date")
    @Nullable
    private LocalDate dueDate;

    @Column(name = "done", columnDefinition = "boolean default false")
    private Boolean done = false;
    
    @Column(name = "in_trash", columnDefinition = "boolean default false")
    private boolean inTrash = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    @NotNull
    private Person person;

    @Override
    public @Nullable Long getId() {
        return id;
    }

    public void setDone(Boolean done){
        this.done = done;
    }

    public Boolean isDone(){
        return this.done;
    }

    public void setInTrash(boolean inTrash) {
        this.inTrash = inTrash;
    }

    public boolean isInTrash() {
        return inTrash;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public @Nullable LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(@Nullable LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }
}
