package smp.group.task.tracking.Service;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import smp.group.task.tracking.DTO.TaskDto;
import smp.group.task.tracking.DTO.UserDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {

    String registerUser(UserDto userDto );

    String loginUser (UserDto dto) throws BadRequestException;

    String forgetUserPassword(UserDto userDto);

    ResponseEntity<Map<String ,String>> addSubTaskInTask(TaskDto taskDto, UUID id);

    ResponseEntity<Map<String ,String>> addTask(TaskDto taskDto);
}
