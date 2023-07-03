package project.model.dto.request;

import lombok.Data;

@Data
public class SliderRequest {
    private String name;
    private String sliderDescription;
    private String sliderBackground;
    private int status;
}
