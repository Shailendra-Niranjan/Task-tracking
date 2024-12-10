package smp.group.task.tracking.Controller;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import smp.group.task.tracking.DTO.TaskDto;
import smp.group.task.tracking.Entity.Team;
import smp.group.task.tracking.Repository.TeamRepo;
import smp.group.task.tracking.Service.TeamService;
import smp.group.task.tracking.Service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;

    @Autowired
    TeamRepo teamRepo;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createTeam(@RequestParam List<String> users ,@RequestParam String teamName) throws MessagingException, IOException {
       return teamService.createTeam(users , teamName);
    }

    @PostMapping("/addUserInTeamReq")
    public ResponseEntity<Map<String, String>> addUserInTeam(@RequestParam  String teamId, @RequestParam String useranme) throws MessagingException, IOException {
        return teamService.addUserInTeam(teamId, useranme);
    }
    @PostMapping("/acceptTeamCreationsReq")
    public ResponseEntity<Map<String, String>> acceptTeamCreationsReq(@RequestParam  String teamId){
        return teamService.acceptTeamCreationsReq(teamId);
    }
    @PostMapping("/makeAdmin")
   public ResponseEntity<Map<String, String>> makeAdmin(@RequestParam String teamId, @RequestParam String userEmail){
        return teamService.makeAdmin(teamId,userEmail);
    }
    @PostMapping("/addTaskInTeam/{teamId}")
    public   ResponseEntity<Map<String ,String>> addTaskInTeam(@RequestBody TaskDto taskDto, @PathVariable UUID teamId){
        return teamService.addTaskInTeam(taskDto, teamId);
    }
    @PostMapping("/addAssigneInTeamTask")
    public ResponseEntity<Map<String ,String>> addAssigneInTeamTask(@RequestParam String assigne, @RequestParam UUID teamId, @RequestParam UUID taskId){
        return teamService.addAssigneInTeamTask(assigne, teamId, taskId);
    }
    @PostMapping("/addDependencyInTeamTask")
    public ResponseEntity<Map<String ,String>> addDependencyInTeamTask(@RequestParam String dependency, @RequestParam UUID teamId, @RequestParam UUID taskId){
        return teamService.addDependencyInTeamTask(dependency, teamId, taskId);
    }
    @PostMapping("/addAssigneInTeamSubTask")
    public ResponseEntity<Map<String ,String>> addAssigneInTeamSubTask(@RequestParam String assigne, @RequestParam UUID teamId,@RequestParam UUID subTaskId){
        return teamService.addAssigneInTeamSubTask(assigne, teamId, subTaskId);
    }
    @PostMapping("/addDependencyInTeamSubTask")
    public ResponseEntity<Map<String ,String>> addDependencyInTeamSubTask( @RequestParam String dependency, @RequestParam UUID teamId, @RequestParam UUID subTaskId){
        return teamService.addDependencyInTeamSubTask(dependency, teamId, subTaskId);
    }
    @PostMapping("/addSubTaskInTask/{id}")
   public ResponseEntity<Map<String ,String>> addSubTaskInTask(@RequestBody TaskDto taskDto, @PathVariable UUID id){
        return userService.addSubTaskInTask(taskDto ,id);
    }
    @GetMapping
    public List<Team> getALlTeam(){
       return teamRepo.findAll();
    }


}
