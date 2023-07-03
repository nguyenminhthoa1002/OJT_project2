package project.bussiness.service;

import org.springframework.http.ResponseEntity;
import project.model.dto.request.ChangePassword;
import project.model.dto.request.LogInRequest;
import project.model.dto.request.UserRequest;
import project.model.dto.response.UserResponse;
import project.model.entity.Users;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends RootService<Users,Integer, UserRequest, UserResponse> {
    Users findByEmail(String email);
    ResponseEntity<?> register(UserRequest userRequest);
    Users saveUpdate(Users users);
    ResponseEntity<?> blockedUser(int userId, int blockedDays);
    ResponseEntity<?> unBlockedUser(int userId);
    ResponseEntity<?> logIn (LogInRequest logInRequest);
    ResponseEntity<?> logOut();
    List<UserResponse> getAllUserForModerator();
    ResponseEntity<?> updateUserForModerator(int userId, UserRequest userRequest);
    ResponseEntity<?> updateUserForUser(int userId, UserRequest userRequest);
    UserResponse findUserByIdForClient();
    ResponseEntity<?> changePassword(ChangePassword changePassword);
    ResponseEntity<?> forgotPassword(String userEmail, HttpServletRequest request);
    ResponseEntity<?> creatNewPass(String token, String newPassword);
}
