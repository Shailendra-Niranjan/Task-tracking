package smp.group.task.tracking.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {

    private String title;
    private String description;
    private Date startAt;
    private Date endAt;

}
