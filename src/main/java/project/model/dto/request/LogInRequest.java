package project.model.dto.request;

import lombok.Data;

@Data
public class LogInRequest {
    private String userName;
    private String password;
}
