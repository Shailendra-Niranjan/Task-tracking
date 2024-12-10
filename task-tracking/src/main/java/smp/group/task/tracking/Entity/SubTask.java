package smp.group.task.tracking.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class SubTask extends BaseEntity {

    private String title;
    private String description;
    private String assigne;
    private String dependency;
    private Date startAt;
    private Date endAt;
    private String createdBy;

    @ManyToOne
    @JsonBackReference
    private Task task;
}
