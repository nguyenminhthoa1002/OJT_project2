package project.model.dto.response;

import lombok.Data;

@Data
public class WishResponse extends RootResponse {
    private ProductResponse productResponse;
}
