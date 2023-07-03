package project.model.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserRequest {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String state;
    private String city;
    private String county;
    private String phone;
    private String avatar;
    private LocalDate birtDate;
    private Set<String> listRoles;
    private boolean userStatus;
}
