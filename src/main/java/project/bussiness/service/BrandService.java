package project.bussiness.service;

import project.model.dto.request.BrandRequest;
import project.model.dto.response.BrandResponse;
import project.model.entity.Brand;

import java.time.LocalDateTime;
import java.util.List;

public interface BrandService extends RootService<Brand,Integer, BrandRequest, BrandResponse> {
    List<BrandResponse> getFeatureBrand(LocalDateTime startDate, LocalDateTime endDate, int size);
}
