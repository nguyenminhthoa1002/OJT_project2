package project.model.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class JwtResponse {
    private String type = "Bearer";
    private String token;
    private int userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String state;
    private String city;
    private String country;
    private String phone;
    private String avatar;
    private LocalDate birtDate;
    private boolean userStatus;
    private int ranking;
    private List<String> listRoles;
    private CartResponse cartResponse;
//    private List<CouponResponse> couponResponseList;

    public JwtResponse(String token, int userId, String userName, String firstName, String lastName, String email, String address, String state, String city, String country, String phone, String avatar, LocalDate birtDate, boolean userStatus, int ranking, List<String> listRoles, CartResponse cartResponse/*, List<CouponResponse> couponResponseList */) {
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.state = state;
        this.city = city;
        this.country = country;
        this.phone = phone;
        this.avatar = avatar;
        this.birtDate = birtDate;
        this.userStatus = userStatus;
        this.ranking = ranking;
        this.listRoles = listRoles;
        this.cartResponse = cartResponse;
        //        this.couponResponseList=couponResponseList;
    }
}
