package project.model.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductFeatureRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int size;
}
