package smp.group.task.tracking.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private String role;
    private  String name;
    private  String address;
    private  String contact;
    private  String city;
    private  String state;
    private  String pincode;
}
