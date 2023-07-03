package project.model.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FilterProductRequest {
    private List<Integer> locationId = new ArrayList<>();
    private float max;
    private float min;
    private List<Integer> starPoint = new ArrayList<>();
}
