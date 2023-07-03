package project.model.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import project.model.entity.Roles;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserResponse {
    @JsonIgnore
    private int userId;
    private String userName;
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
    @JsonIgnore
    private Set<Roles> listRoles;
    @JsonIgnore
    private boolean userStatus;
}
