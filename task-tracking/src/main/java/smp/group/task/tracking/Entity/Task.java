package smp.group.task.tracking.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Task extends BaseEntity{

    private String title;
    private String description;
    private String assigne;
    private String dependency;
    private Date startAt;
    private Date endAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SubTask> subTaskList;

    @ManyToOne
    @JsonBackReference
    private Team team;

    @ManyToOne
    @JsonBackReference
    private Users users;
}
