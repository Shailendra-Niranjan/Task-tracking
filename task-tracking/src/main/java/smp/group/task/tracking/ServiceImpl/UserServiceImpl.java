package smp.group.task.tracking.ServiceImpl;


import com.google.gson.Gson;
import jakarta.mail.MessagingException;
import org.apache.catalina.User;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import smp.group.task.tracking.DTO.TaskDto;
import smp.group.task.tracking.DTO.UserDto;
import smp.group.task.tracking.Entity.Role;
import smp.group.task.tracking.Entity.SubTask;
import smp.group.task.tracking.Entity.Task;
import smp.group.task.tracking.Entity.Users;
import smp.group.task.tracking.Repository.SubtaskRepo;
import smp.group.task.tracking.Repository.TaskRepo;
import smp.group.task.tracking.Repository.UserRepository;
import smp.group.task.tracking.Security.JWTService;
import smp.group.task.tracking.Security.LoggedInUser;
import smp.group.task.tracking.Security.UserDeatilsServices;
import smp.group.task.tracking.Service.CloudinaryImageService;
import smp.group.task.tracking.Service.EmailService;
import smp.group.task.tracking.Service.UserService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService {

    private BCryptPasswordEncoder encoder  = new BCryptPasswordEncoder(11);

    // Character pool for the password
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "abcdefghijklmnopqrstuvwxyz" +
                    "0123456789" +
                    "!@#$%^&*()-_+=<>?";

    private static final SecureRandom RANDOM = new SecureRandom();


    @Autowired
    AuthenticationManager authManager;

    @Autowired
    JWTService jwtService ;
    @Autowired
    private UserDeatilsServices userDeatilsServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    Gson gson;

    @Autowired
    CloudinaryImageService cloudinaryImageService;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    SubtaskRepo subtaskRepo;

    @Override
    public String registerUser(UserDto userDto) {
        Users usersExist = userRepository.findUsersByEmail(userDto.getEmail());
        if (usersExist!=null){
            return "User already Exist !";
        }
        Users users = new Users();
        users.setEmail(userDto.getEmail());
        users.setPassword(encoder.encode(userDto.getPassword()));
        users.setAddress(userDto.getAddress());
        users.setContact(userDto.getContact());
        users.setName(userDto.getName());
        users.setCity(userDto.getCity());
        users.setState(userDto.getState());
        users.setPincode(userDto.getPincode());
        users.setRole(Role.USER_ROLE);

        try {
            userRepository.save(users);
            emailService.userCreationMail(users.getEmail() ,"Account Created" , userDto.getPassword() ,users.getName());

        } catch (IOException e) {
            System.out.println(e);
            return "IOException occur while sending mail";
        } catch (MessagingException e) {
            System.out.println(e);
            return "MessagingException occur while sending mail";
        }catch (Exception e){
            return e.getLocalizedMessage();
        }
        return "Register Successfully !";
    }

    @Override
    public String loginUser(UserDto dto) throws BadRequestException {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        System.out.println(authentication.getAuthorities().toString());
        String token = jwtService.generateToken(dto.getEmail() , String.valueOf(authentication.getAuthorities().stream().toList().get(0)));
        System.out.println(token);
        return token;
    }

    @Override
    public String forgetUserPassword(UserDto userDto) {

      LoggedInUser loggedInUser  = (LoggedInUser) userDeatilsServices.loadUserByUsername(userDto.getEmail());

      String newPassword = generatePassword();
      Users users = loggedInUser.getUsers();
      users.setPassword(encoder.encode(newPassword));
      userRepository.save(users);
        try {
            emailService.sendResetPassword(users.getEmail() ,"Forget Password" , newPassword , users.getName());
        } catch (IOException e) {
            System.out.println(e);
            return "IOException occur while sending mail";
        } catch (MessagingException e) {
            System.out.println(e);
            return "MessagingException occur while sending mail";
        }
        return "New Password sent to your email";
    }



    public static String generatePassword() {
        StringBuilder password = new StringBuilder(10); // Fixed length: 10

        // Generate 10 random characters from the pool
        for (int i = 0; i < 10; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }


    public ResponseEntity<Map<String ,String>> addTask(TaskDto taskDto){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
       Users user = userRepository.findUsersByEmail(username);
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStartAt(taskDto.getStartAt());
        task.setEndAt(taskDto.getEndAt());
        user.getTasks().add(task);
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("Message" ,"Task added successfully"));
    }

    public ResponseEntity<Map<String ,String>> addSubTaskInTask(TaskDto taskDto, UUID id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Users users = userRepository.findUsersByEmail(username);
        Optional<Task> task = taskRepo.findById(id);
        if(!task.isPresent()){
            return ResponseEntity.badRequest().body(Map.of("Message", "Invalid Task Id"));
        }
        SubTask subTask = new SubTask();
        subTask.setTitle(taskDto.getTitle());
        subTask.setDescription(taskDto.getDescription());
        subTask.setStartAt(taskDto.getStartAt());
        subTask.setEndAt(taskDto.getEndAt());
        subTask.setCreatedBy(username);
        task.get().getSubTaskList().add(subTask);
        taskRepo.save(task.get());
        return ResponseEntity.ok(Map.of("Message" ,"Sub Task added successfully"));

    }


}
