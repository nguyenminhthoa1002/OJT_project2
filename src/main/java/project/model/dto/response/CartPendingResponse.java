package project.model.dto.response;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CartPendingResponse {
    private List<CartDetailResponse> cartDetailResponseList = new ArrayList<>();
    private float summary;
}
