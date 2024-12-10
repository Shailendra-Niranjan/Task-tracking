package smp.group.task.tracking.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smp.group.task.tracking.DTO.TaskDto;
import smp.group.task.tracking.Service.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/addSubTaskInTask/{id}")
    public ResponseEntity<Map<String ,String>> addSubTaskInTask(@RequestBody TaskDto taskDto, @PathVariable UUID id){
        return userService.addSubTaskInTask(taskDto ,id);
    }

    @PostMapping("/addTask")
    ResponseEntity<Map<String ,String>> addTask(@RequestBody TaskDto taskDto){
        return userService.addTask(taskDto);
    }
}
