package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import project.bussiness.service.CartService;
import project.bussiness.service.CouponService;
import project.bussiness.service.PasswordResetTokenService;
import project.bussiness.service.UserService;
import project.model.dto.request.ChangePassword;
import project.model.dto.request.LogInRequest;
import project.model.dto.request.UserRequest;
import project.model.entity.*;
import project.model.dto.response.CartResponse;
import project.model.dto.response.JwtResponse;
import project.model.dto.response.UserResponse;
import project.model.entity.Cart;
import project.model.entity.ERole;
import project.model.entity.Roles;
import project.model.entity.Users;
import project.model.regex.RegexValidate;
import project.model.sendEmail.ProvideSendEmail;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.*;
import project.security_jwt.CustomUserDetails;
import project.security_jwt.CustomUserDetailsService;
import project.security_jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private RoleRepository roleRepository;
    private CartRepository cartRepository;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider tokenProvider;
    private CartService cartService;
    private CouponService couponService;
    private TokenLogInReposirory tokenLogInReposirory;
    private ProvideSendEmail provideSendEmail;
    private PasswordResetTokenService passResetService;
    private CustomUserDetailsService customUserDetailsService;

    private ProductRepository productRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Users> page = userRepository.findAll(pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public UserResponse saveOrUpdate(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserResponse update(Integer id, UserRequest userRequest) {
        Users userUpdate = userRepository.findById(id).get();
        userUpdate.setUserStatus(userRequest.isUserStatus());
        Set<String> strRoles = userRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            //User quyen mac dinh
            Roles userRole = (Roles) roleRepository.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = null;
                        try {
                            adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = null;
                        try {
                            modRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = null;
                        try {
                            userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(userRole);
                }
            });
        }
        userUpdate.setListRoles(listRoles);
        userRepository.save(userUpdate);
        return mapPoJoToResponse(userUpdate);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            Users userDelete = findById(id);
            userDelete.setUserStatus(false);
            userRepository.save(userDelete);
            return ResponseEntity.ok().body(Message.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public List<Users> findAll() {
        return null;
    }

    @Override
    public List<UserResponse> getAllForClient() {
        return null;
    }

    @Override
    public Users findById(Integer id) {
        return userRepository.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Users> page = userRepository.findByFirstNameContaining(name, pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;

    }

    @Override
    public Users mapRequestToPoJo(UserRequest userRequest) {
        return null;
    }

    @Override
    public UserResponse mapPoJoToResponse(Users users) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(users.getUserId());
        userResponse.setUserName(users.getUserName());
        userResponse.setFirstName(users.getFirstName());
        userResponse.setLastName(users.getLastName());
        userResponse.setEmail(users.getEmail());
        userResponse.setAvatar(users.getAvatar());
        userResponse.setCity(users.getCity());
        userResponse.setState(users.getState());
        userResponse.setAddress(users.getAddress());
        userResponse.setCounty(users.getCountry());
        userResponse.setPhone(users.getPhone());
        userResponse.setBirtDate(users.getBirtDate());
        userResponse.setListRoles(users.getListRoles());
        userResponse.setUserStatus(users.isUserStatus());
        return userResponse;
    }

    @Override
    public Users findByEmail(String email) {
        return null;
    }

    @Override
    public ResponseEntity<?> register(UserRequest userRequest) {
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            return ResponseEntity.badRequest().body(Message.ERROR_EXISTED_USERNAME);
        }
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Message.ERROR_EXISTED_EMAIL);
        }
        if (!RegexValidate.checkRegexEmail(userRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Message.ERROR_INVALID_EMAIL);
        }
        if (!RegexValidate.checkRegexPhone(userRequest.getPhone())) {
            return ResponseEntity.badRequest().body(Message.ERROR_INVALID_PHONE);
        }
        if (!RegexValidate.checkRegexPassword(userRequest.getPassword())) {
            return ResponseEntity.badRequest().body(Message.ERROR_INVALID_PASSWORD);
        }
        Users user = new Users();
        user.setUserName(userRequest.getUserName());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setAvatar(userRequest.getAvatar());
        user.setLastName(userRequest.getLastName());
        user.setFirstName(userRequest.getFirstName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        user.setAddress(userRequest.getAddress());
        user.setState(userRequest.getState());
        user.setCity(userRequest.getCity());
        user.setCountry(userRequest.getCounty());
        user.setBirtDate(userRequest.getBirtDate());
        user.setRanking(0);
        user.setUserStatus(true);
        Set<String> strRoles = userRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles == null) {
            //User quyen mac dinh
            Roles userRole = (Roles) roleRepository.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
            listRoles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Roles adminRole = null;
                        try {
                            adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(adminRole);
                    case "moderator":
                        Roles modRole = null;
                        try {
                            modRole = roleRepository.findByRoleName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(modRole);
                    case "user":
                        Roles userRole = null;
                        try {
                            userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException(Message.ERROR_ROLE_NOT_FOUND));
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                        listRoles.add(userRole);
                }
            });
        }
        user.setListRoles(listRoles);
        Users result = saveUpdate(user);
        Cart cart = new Cart();
        cart.setUsers(result);
        cart.setStatus(0);
        cartRepository.save(cart);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public Users saveUpdate(Users users) {
        return userRepository.save(users);
    }

    @Override
    public ResponseEntity<?> blockedUser(int userId, int blockedDays) {
        Users users = userRepository.findById(userId).get();
        LocalDate blockDate = LocalDate.now().plusDays(blockedDays);
        users.setBlockedDate(blockDate);
        users.setUserStatus(false);
        userRepository.save(users);
        return ResponseEntity.ok().body("Blocked in " + blockedDays + " days");
    }

    @Override
    public ResponseEntity<?> unBlockedUser(int userId) {
        Users users = userRepository.findById(userId).get();
        users.setUserStatus(true);
        users.setBlockedDate(null);
        userRepository.save(users);
        return ResponseEntity.ok().body(Message.UNBLOCK_USER_SUCCESS);
    }

    @Override
    public ResponseEntity<?> logIn(LogInRequest logInRequest) {
        Users users = userRepository.findUsersByUserName(logInRequest.getUserName());
        LocalDate now = LocalDate.now();
        if (users.getBlockedDate() != null && now.isAfter(users.getBlockedDate())) {
            users.setUserStatus(true);
            userRepository.save(users);
        }
        if (users.isUserStatus()) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logInRequest.getUserName(), logInRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            //Sinh JWT tra ve client
            String jwt = tokenProvider.generateToken(customUserDetail);
            //Lay cac quyen cua user
            List<String> listRoles = customUserDetail.getAuthorities().stream()
                    .map(item -> item.getAuthority()).collect(Collectors.toList());
            Cart cart = customUserDetail.getListCart().get(customUserDetail.getListCart().size() - 1);
            CartResponse cartResponse = cartService.mapPoJoToResponse(cart);
//            List<CouponResponse> couponResponses = couponService.getAllForClient();
            JwtResponse response = new JwtResponse(jwt, customUserDetail.getUserId(), customUserDetail.getUsername(), customUserDetail.getFirstName(), customUserDetail.getLastName(),
                    customUserDetail.getEmail(), customUserDetail.getAddress(), customUserDetail.getState(), customUserDetail.getCity(), customUserDetail.getCounty(),
                    customUserDetail.getPhone(), customUserDetail.getAvatar(), customUserDetail.getBirtDate(), customUserDetail.isUserStatus(), customUserDetail.getRanking(),
                    listRoles, cartResponse/*,couponResponses*/);
            TokenLogIn tokenLogIn = new TokenLogIn();
            tokenLogIn.setName(jwt);
            tokenLogIn.setUsers(users);
            tokenLogIn.setStatus(1);
            tokenLogInReposirory.save(tokenLogIn);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(Message.ERROR_LOCKED_USER);
        }
    }

    @Override
    public ResponseEntity<?> logOut() {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        TokenLogIn tokenLogIn = tokenLogInReposirory.findByUsers_UserId(users.getUserId());
        tokenLogInReposirory.deleteById(tokenLogIn.getId());
        return ResponseEntity.ok().body(Message.LOGOUT_SUCCESS);
    }

    @Override
    public List<UserResponse> getAllUserForModerator() {
        List<Users> usersForModerator = new ArrayList<>();
        List<Users> listUser = userRepository.findAll();
        Set<Roles> roleUser = new HashSet<>();
        Roles userRole = roleRepository.findById(3).get();
        roleUser.add(userRole);
        for (Users user : listUser) {
            if (user.getListRoles().containsAll(roleUser) && user.getListRoles().size() == 1) {
                usersForModerator.add(user);
            }

        }
        List<UserResponse> responses = usersForModerator.stream()
                .map(this::mapPoJoToResponse).collect(Collectors.toList());
        return responses;
    }

    @Override
    public ResponseEntity<?> updateUserForModerator(int userId, UserRequest userRequest) {
        Users user = findById(userId);
        Set<Roles> roleUser = new HashSet<>();
        Roles userRole = roleRepository.findById(3).get();
        roleUser.add(userRole);
        if (user.getListRoles().containsAll(roleUser) && user.getListRoles().size() == 1) {
            user.setUserStatus(userRequest.isUserStatus());
            userRepository.save(user);
            UserResponse response = mapPoJoToResponse(user);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public ResponseEntity<?> updateUserForUser(int userId, UserRequest userRequest) {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        if (users.getUserId() == userId) {
            try {
                if (userRepository.existsByEmail(userRequest.getEmail())) {
                    return ResponseEntity.badRequest().body(Message.ERROR_EXISTED_EMAIL);
                }
                if (!RegexValidate.checkRegexEmail(userRequest.getEmail())) {
                    return ResponseEntity.badRequest().body(Message.ERROR_INVALID_EMAIL);
                }
                if (!RegexValidate.checkRegexPhone(userRequest.getPhone())) {
                    return ResponseEntity.badRequest().body(Message.ERROR_INVALID_PHONE);
                }
                Users userUpdateUser = findById(userId);
                userUpdateUser.setFirstName(userRequest.getFirstName());
                userUpdateUser.setLastName(userRequest.getLastName());
                userUpdateUser.setPhone(userRequest.getPhone());
                userUpdateUser.setAddress(userRequest.getAddress());
                userUpdateUser.setEmail(userRequest.getEmail());
                userUpdateUser.setCountry(userRequest.getCounty());
                userUpdateUser.setCity(userRequest.getCity());
                userUpdateUser.setState(userRequest.getState());
                userUpdateUser.setBirtDate(userRequest.getBirtDate());
                userUpdateUser.setAvatar(userRequest.getAvatar());
                userRepository.save(userUpdateUser);
                UserResponse response = mapPoJoToResponse(userUpdateUser);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(Message.ERROR_400);
            }
        } else {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }


    }

    @Override
    public UserResponse findUserByIdForClient() {
        CustomUserDetails userIsLoggingIn = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userRepository.findUsersByUserName(userIsLoggingIn.getUsername());
        return mapPoJoToResponse(findById(users.getUserId()));
    }

    @Override
    public ResponseEntity<?> changePassword(ChangePassword changePassword) {
        try {
            CustomUserDetails usersChangePass = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Users users = userRepository.findUsersByUserName(usersChangePass.getUsername());

            String userName = changePassword.getUserName();
            String oldPass = changePassword.getOldPass();
            String newPass = changePassword.getNewPass();

            if (!RegexValidate.checkRegexPassword(newPass)) {
                return ResponseEntity.badRequest().body(Message.ERROR_INVALID_PASSWORD);
            }

            if (usersChangePass.getUsername().equals(userName) && BCrypt.checkpw(oldPass, usersChangePass.getPassword())) {
                users.setPassword(encoder.encode(newPass));
                userRepository.save(users);
            }
            return ResponseEntity.ok(Message.CHANGE_PASSWORD_SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }

    }

    @Override
    public ResponseEntity<?> forgotPassword(String userEmail, HttpServletRequest request) {
        if (userRepository.existsByEmail(userEmail)) {
            Users users = userRepository.findByEmail(userEmail);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(users.getUserName());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = UUID.randomUUID().toString();
            PasswordResetToken myToken = new PasswordResetToken();
            myToken.setToken(token);
            String mess = "token is valid for 5 minutes.\n" + "Your token: " + token;
            myToken.setUsers(users);
            LocalDateTime now = LocalDateTime.now();
            myToken.setStartDate(now);
            passResetService.saveOrUpdate(myToken);
            provideSendEmail.sendSimpleMessage(users.getEmail(),
                    "Reset your password", mess);
            return ResponseEntity.ok("Email sent! Please check your email");
        } else {
            return ResponseEntity.badRequest().body(Message.EMAIL_NOT_SENT);
        }
    }

    @Override
    public ResponseEntity<?> creatNewPass(String token, String newPassword) {
        if (!RegexValidate.checkRegexPassword(newPassword)) {
            return ResponseEntity.badRequest().body(Message.ERROR_INVALID_PASSWORD);
        }
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PasswordResetToken passwordResetToken = passResetService.getLastTokenByUserId(userDetails.getUserId());
        long date1 = passwordResetToken.getStartDate().toLocalDate().toEpochDay() + 1800000;
        long date2 = LocalDateTime.now().toLocalDate().toEpochDay();
        if (date2 > date1) {
            return ResponseEntity.badRequest().body("Expired Token ");
        } else {
            if (passwordResetToken.getToken().equals(token)) {
                Users users = userRepository.findById(userDetails.getUserId()).get();
                users.setPassword(encoder.encode(newPassword));
                userRepository.save(users);
                return ResponseEntity.ok().body("update password successfully");
            } else {
                return ResponseEntity.badRequest().body("token is fail ");
            }
        }
    }

}
