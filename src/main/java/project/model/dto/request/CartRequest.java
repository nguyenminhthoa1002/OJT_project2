package project.model.dto.request;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
@Data
public class CartRequest {
    private String firstName;
    private String lastName;
    private  String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private  String state;
    private String note;
    private float total;
    private int discount;
    private float shipping;
    private float tax;
    private List<CartDetailRequest> detailRequests= new ArrayList<>();
}
