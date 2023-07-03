package project.model.dto.request;

import lombok.Data;

@Data
public class CartConfirmRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private String state;
    private String note;
    private int couponId;
}
