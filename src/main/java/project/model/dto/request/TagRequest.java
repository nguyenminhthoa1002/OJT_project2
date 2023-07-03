package project.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class TagRequest {
    private String name;
  private int status;

}
