package project.model.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import project.model.entity.Users;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartResponse extends RootResponse{
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String country;
    private String city;
    private String state;
    private float subTotal;
    private float discount;
    private float shipping;
    private float tax;
    private float total;
    private List<CartDetailResponse> detailResponses= new ArrayList<>();
    @JsonIgnore
    private Users users;
}
