package smp.group.task.tracking.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Users extends BaseEntity{

    private String email;
    private String password;
    @Enumerated
    private Role role;
    private  String name;
    private  String address;
    private  String contact;
    private  String city;
    private  String state;
    private  String pincode;
    @OneToMany(mappedBy = "users",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Task>  tasks;


}
