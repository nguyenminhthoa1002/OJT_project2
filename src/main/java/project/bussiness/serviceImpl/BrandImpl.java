package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.BrandService;
import project.bussiness.service.ProductService;
import project.model.dto.request.BrandRequest;
import project.model.dto.response.BrandResponse;
import project.model.dto.response.ProductResponse;
import project.model.entity.Brand;
import project.model.entity.Product;

import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.BrandRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BrandImpl implements BrandService {
    private BrandRepository brandRepository;
    private ProductService productService;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Brand> brands = brandRepository.findAll(pageable);
        Map<String, Object> result = Utility.returnResponse(brands);
        return result;
    }

    @Override
    public BrandResponse saveOrUpdate(BrandRequest brandRequest) {
        Brand brand = mapRequestToPoJo(brandRequest);
        Brand brandNew = brandRepository.save(brand);
        BrandResponse brandResponse = mapPoJoToResponse(brandNew);
        return brandResponse;
    }

    @Override
    public BrandResponse update(Integer id, BrandRequest brandRequest) {
        Brand brand = mapRequestToPoJo(brandRequest);
        brand.setId(id);
        Brand brandOfUpdate = brandRepository.save(brand);
        BrandResponse brandResponse = mapPoJoToResponse(brandOfUpdate);
        return brandResponse;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            Brand brandDelete = brandRepository.findById(id).get();
            brandDelete.setStatus(0);
            brandRepository.save(brandDelete);
            return ResponseEntity.ok().body(Message.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public List<BrandResponse> getAllForClient() {
        return null;
    }

    @Override
    public Brand findById(Integer id) {
        return brandRepository.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Brand> page = brandRepository.findByNameContaining(name, pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public Brand mapRequestToPoJo(BrandRequest brandRequest) {
        Brand brand = new Brand();
        brand.setBrandLogo(brandRequest.getBrandLogo());
        brand.setName(brandRequest.getName());
        brand.setStatus(brandRequest.getStatus());
        return brand;
    }

    @Override
    public BrandResponse mapPoJoToResponse(Brand brand) {
        BrandResponse brandResponse = new BrandResponse();
        brandResponse.setId(brand.getId());
        brandResponse.setName(brand.getName());
        brandResponse.setBrandLogo(brand.getBrandLogo());
        brandResponse.setStatus(brand.getStatus());
        return brandResponse;
    }

    @Override
    public List<BrandResponse> getFeatureBrand(LocalDateTime startDate, LocalDateTime endDate, int size) {
        List<ProductResponse> productResponseList = productService.getFeatureProduct(startDate, endDate, size);
        List<Product> productList = new ArrayList<>();
        for (ProductResponse pro : productResponseList) {
            productList.add(productService.findById(pro.getId()));
        }
        Set<Brand> brandList = brandRepository.findByProductListIn(productList);
        List<BrandResponse> responses = brandList.stream()
                .map(this::mapPoJoToResponse).collect(Collectors.toList());
        return responses;
    }
}
