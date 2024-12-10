package smp.group.task.tracking.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Team extends BaseEntity{

    private String teamName;
    @OneToMany
    private List<Users> users;

    @OneToMany
    private List<Users> admins;

    @ManyToOne
    private Users creator;

    @OneToMany(mappedBy = "team" , cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    private List<Task> tasks;

     public Team(){
         this.users = new ArrayList<>();
         this.admins = new ArrayList<>();
         this.tasks = new ArrayList<>();
     }
}
