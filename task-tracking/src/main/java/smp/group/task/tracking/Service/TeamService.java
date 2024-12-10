package smp.group.task.tracking.Service;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import smp.group.task.tracking.DTO.TaskDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TeamService {

    ResponseEntity<Map<String ,String>> createTeam(List<String> users , String teamName) throws MessagingException, IOException;
    ResponseEntity<Map<String , String>> addUserInTeam(String teamId, String useranme) throws MessagingException, IOException;
    ResponseEntity<Map<String, String>> acceptTeamCreationsReq(String teamId);
    ResponseEntity<Map<String, String>> makeAdmin(String teamId , String userEmail);
    ResponseEntity<Map<String ,String>> addTaskInTeam(TaskDto taskDto, UUID teamId);
    ResponseEntity<Map<String ,String>> addAssigneInTeamTask(String assigne, UUID teamId, UUID taskId);
    ResponseEntity<Map<String ,String>> addDependencyInTeamTask(String dependency, UUID teamId, UUID taskId);
    ResponseEntity<Map<String ,String>> addAssigneInTeamSubTask(String assigne, UUID teamId, UUID subTaskId);
    ResponseEntity<Map<String ,String>> addDependencyInTeamSubTask(String dependency, UUID teamId, UUID subTaskId);
}
