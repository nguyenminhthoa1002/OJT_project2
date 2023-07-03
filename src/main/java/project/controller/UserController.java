package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.UserService;
import project.model.dto.request.ChangePassword;
import project.model.dto.request.LogInRequest;
import project.model.dto.request.UserRequest;
import project.model.dto.response.UserResponse;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRequest userRequest) {
        try {
            return userService.register(userRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PostMapping("logIn")
    public ResponseEntity<?> logIn(@RequestBody LogInRequest logInRequest) {
        try {
            return userService.logIn(logInRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    //    -------------------   ROLE: ADMIN   -------------------------
    @PutMapping("/block/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> blockedUser(@PathVariable int userId, @RequestParam int blockedDays) {
        try {
            return userService.blockedUser(userId, blockedDays);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/unblockUser/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> unBlockedUser(@PathVariable int userId) {
        try {
            return userService.unBlockedUser(userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PostMapping("logOut")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> logOut() {
        try {
            return userService.logOut();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping()
    public ResponseEntity<?> pageAndSortUsers(@RequestParam Map<String, String> headers) {
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String, Object> result = userService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("search")
    public ResponseEntity<?> searchSortPaging(@RequestParam Map<String, String> headers) {
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String, Object> result = userService.findByName(headers.get("searchName"), pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        try {
            return userService.register(userRequest);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/{userId}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest, @PathVariable("userId") int userId) {
        try {
            UserResponse userResponse = userService.update(userId, userRequest);
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @DeleteMapping("/{userId}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") int userId) {
        return userService.delete(userId);
    }

    //    --------------------- ROLE : MODERATOR ----------------------------
    @GetMapping("getAllUserForModerator")
//    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> getAllUserForModerator() {
        try {
            List<UserResponse> result = userService.getAllUserForModerator();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("updateUserForModerator/{userId}")
//    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> updateUserForModerator(@PathVariable("userId") int userId, @RequestBody UserRequest userRequest) {
        return userService.updateUserForModerator(userId, userRequest);
    }

    //    ----------------------- ROLE : USER ------------------------
    @PutMapping("updateUserForUser/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserForUser(@PathVariable("userId") int userId, @RequestBody UserRequest userRequest) {
        return userService.updateUserForUser(userId,userRequest);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> findUserByIdForClient() {
        try {
            UserResponse response = userService.findUserByIdForClient();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PostMapping("changePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePassword changePassword){
        return userService.changePassword(changePassword);
    }

    @PostMapping("forgotPassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String userEmail, HttpServletRequest request){
        return userService.forgotPassword(userEmail,request);
    }

    @PostMapping("/creatNewPass")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> creatNewPass(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword) {
        return userService.creatNewPass(token,newPassword);
    }
}
