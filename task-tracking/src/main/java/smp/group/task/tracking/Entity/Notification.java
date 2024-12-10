package smp.group.task.tracking.Entity;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Notification extends BaseEntity {

    private String title;
    private String type;
    private UUID teamId;
    private String teamName;
    private String taskId;
    private String SubtaskId;
    private String fromUser;
    private String toUser;
    private boolean read;
    private boolean delete;

}
