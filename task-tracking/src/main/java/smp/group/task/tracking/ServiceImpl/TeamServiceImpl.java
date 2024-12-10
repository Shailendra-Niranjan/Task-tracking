package smp.group.task.tracking.ServiceImpl;

import jakarta.mail.MessagingException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import smp.group.task.tracking.DTO.TaskDto;
import smp.group.task.tracking.Entity.*;
import smp.group.task.tracking.Repository.*;
import smp.group.task.tracking.Service.TeamService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    TeamRepo teamRepo;

    @Autowired
    NotificationRepo notificationRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    SubtaskRepo subtaskRepo;


    @Override
    public ResponseEntity<Map<String, String>> createTeam(List<String> users , String teamName) throws MessagingException, IOException {
        List<Users> usersList = userRepository.findUsersByEmails(users);
        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users creator = userRepository.findUsersByEmail(loginuser);
        Team team = new Team();
        team.setCreator(creator);
        team.getAdmins().add(creator);
        team.setTeamName(teamName);
        team = teamRepo.save(team);
        //send email , notification
        for (Users u : usersList) {
            Notification notification = new Notification();
            notification.setToUser(u.getEmail());
            notification.setTeamName(teamName);
            notification.setTeamId(team.getId());
            notification.setFromUser(creator.getName());
            notification.setTitle("New Team creation request Team Name" + teamName);
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
            emailService.requestForTeam(u.getEmail() , creator.getEmail(), "Team Request",creator.getName(), u.getName() ,teamName);
        }


        return ResponseEntity.ok(Map.of("Message" ,"Team request sended successfully"));
    }

    @Override
    public ResponseEntity<Map<String, String>> addUserInTeam(String teamId, String useranme) throws MessagingException, IOException {
        Optional<Team> team = teamRepo.findById(UUID.fromString(teamId));
        Users users = userRepository.findUsersByEmail(useranme);
        if(team.get().getAdmins().contains(users)||team.get().getUsers().contains(users)){
            return ResponseEntity.ok(Map.of("Message" , "Request already sended to "+ users.getName()));
        }
        if (team.isPresent()&& users!=null){
            Notification notification = new Notification();
            notification.setToUser(users.getEmail());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(team.get().getCreator().getName());
            notification.setTitle("New Team creation request Team Name" + team.get().getCreator().getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
            emailService.requestForTeam(users.getEmail() , team.get().getCreator().getEmail(), "Team Request",team.get().getCreator().getName(), users.getName() ,team.get().getTeamName());
            return ResponseEntity.ok(Map.of("Message" ,"Team request sended successfully"));
        }
        return  ResponseEntity.badRequest().body(Map.of("Message" ,"User not found"));
    }

    @Override
    public ResponseEntity<Map<String, String>> acceptTeamCreationsReq(String teamId) {
        Optional<Team> team = teamRepo.findById(UUID.fromString(teamId));
        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = userRepository.findUsersByEmail(loginuser);
        if(team.isPresent()&&users!=null){
            team.get().getUsers().add(users);
            teamRepo.save(team.get());
            return ResponseEntity.ok(Map.of("Message" ,"Added successfully"));

        }
        return ResponseEntity.ok(Map.of("Message" ,"Invalid information in notification"));

    }


    public ResponseEntity<Map<String, String>> makeAdmin(String teamId , String userEmail) {
        Optional<Team> team = teamRepo.findById(UUID.fromString(teamId));
        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = userRepository.findUsersByEmail(loginuser);
        if (users!=null && !team.get().getAdmins().contains(users)){
            return ResponseEntity.ok(Map.of("Message" ,"You are not admin"));
        }
        Users makeAdmin = userRepository.findUsersByEmail(userEmail);

        if(team.isPresent()&&makeAdmin!=null){
            if(team.get().getUsers().contains(makeAdmin)){
                team.get().getUsers().remove(makeAdmin);
                team.get().getAdmins().add(makeAdmin);
                teamRepo.save(team.get());
                return ResponseEntity.ok(Map.of("Message" ,"Added successfully"));
            }
            else if (team.get().getAdmins().contains(makeAdmin)){
                return ResponseEntity.ok(Map.of("Message" ,"Already admin"));
            }

        }
        return ResponseEntity.ok(Map.of("Message" ,"User not found in team"));

    }

    public ResponseEntity<Map<String ,String>> addTaskInTeam(TaskDto taskDto, UUID teamId){
        Optional<Team> team = teamRepo.findById(teamId);
//        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
//        Users users = userRepository.findUsersByEmail(loginuser);
        if(!team.isPresent()){
            return ResponseEntity.ok(Map.of("Message" ,"Invalid Team "));

        }
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStartAt(taskDto.getStartAt());
        task.setEndAt(taskDto.getEndAt());
        task.setTeam(team.get());
        team.get().getTasks().add(task);
        teamRepo.save(team.get());
        return ResponseEntity.ok(Map.of("Message" ,"Task added successfully"));
    }

    public ResponseEntity<Map<String ,String>> addAssigneInTeamTask(String assigne, UUID teamId, UUID taskId){
        Users users = userRepository.findUsersByEmail(assigne);
        Optional<Team> team = teamRepo.findById(teamId);
        Optional<Task> task = taskRepo.findById(taskId);
        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users logedusers = userRepository.findUsersByEmail(loginuser);
        if (users ==null || !team.isPresent() || !task.isPresent() || logedusers == null){
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid data !"));
        }
        task.get().setAssigne(assigne);
        taskRepo.save(task.get());
        team.get().getUsers().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is assign to" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });
        team.get().getAdmins().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is assign to" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });

        return ResponseEntity.ok(Map.of("Message" ,"Task assign successfully"));

    }

    public ResponseEntity<Map<String ,String>> addDependencyInTeamTask(String dependency, UUID teamId, UUID taskId){
        Users users = userRepository.findUsersByEmail(dependency);
        Optional<Team> team = teamRepo.findById(teamId);
        Optional<Task> task = taskRepo.findById(taskId);
        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users logedusers = userRepository.findUsersByEmail(loginuser);
        if (users ==null || !team.isPresent() || !task.isPresent() || logedusers == null){
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid data !"));
        }
        task.get().setDependency(dependency);
        taskRepo.save(task.get());
        team.get().getUsers().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is dependent on" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });
        team.get().getAdmins().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is dependent on" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });

        return ResponseEntity.ok(Map.of("Message" ,"Add dependency on task"));

    }



    public ResponseEntity<Map<String ,String>> addAssigneInTeamSubTask(String assigne, UUID teamId, UUID subTaskId){
        Users users = userRepository.findUsersByEmail(assigne);
        Optional<Team> team = teamRepo.findById(teamId);
        Optional<SubTask> subTask = subtaskRepo.findById(subTaskId);
        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users logedusers = userRepository.findUsersByEmail(loginuser);
        if (users ==null || !team.isPresent() || !subTask.isPresent() || logedusers == null){
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid data !"));
        }
        subTask.get().setAssigne(assigne);
        subtaskRepo.save(subTask.get());
        team.get().getUsers().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is assign to" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });
        team.get().getAdmins().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is assign to" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });

        return ResponseEntity.ok(Map.of("Message" ,"Sub Task assign successfully"));

    }

    public ResponseEntity<Map<String ,String>> addDependencyInTeamSubTask(String dependency, UUID teamId, UUID subTaskId){
        Users users = userRepository.findUsersByEmail(dependency);
        Optional<Team> team = teamRepo.findById(teamId);
        Optional<SubTask> subTask = subtaskRepo.findById(subTaskId);
        String loginuser = SecurityContextHolder.getContext().getAuthentication().getName();
        Users logedusers = userRepository.findUsersByEmail(loginuser);
        if (users ==null || !team.isPresent() || !subTask.isPresent() || logedusers == null){
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid data !"));
        }
        subTask.get().setDependency(dependency);
        subtaskRepo.save(subTask.get());
        team.get().getUsers().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is dependent on" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });
        team.get().getAdmins().forEach(u->{
            Notification notification = new Notification();
            notification.setToUser(u.getName());
            notification.setTeamName(team.get().getTeamName());
            notification.setTeamId(team.get().getId());
            notification.setFromUser(logedusers.getName());
            notification.setTitle("Task is dependent on" + users.getName());
            notification.setRead(false);
            notification.setDelete(false);
            notificationRepo.save(notification);
        });

        return ResponseEntity.ok(Map.of("Message" ,"Add dependency on task"));

    }



}
