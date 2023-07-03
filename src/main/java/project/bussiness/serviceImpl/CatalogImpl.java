package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.CatalogService;
import project.bussiness.service.ProductService;
import project.model.dto.response.ProductResponse;
import project.model.entity.CartDetail;
import project.model.entity.Product;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.model.dto.request.CatalogRequest;
import project.model.dto.response.CatalogResponse;
import project.model.entity.Catalog;
import project.repository.CartDetailRepository;
import project.repository.CatalogRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogImpl implements CatalogService {
    @Autowired
    CatalogRepository catalogRepo;
    @Autowired
    private ProductService productService;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<CatalogResponse> page = catalogRepo.findAll(pageable).map(this::mapPoJoToResponse);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public CatalogResponse saveOrUpdate(CatalogRequest catalogRequest) {
        Catalog catalog = mapRequestToPoJo(catalogRequest);
        Catalog catalogNew = catalogRepo.save(catalog);
        CatalogResponse catalogResponse = mapPoJoToResponse(catalogNew);
        return catalogResponse;
    }

    @Override
    public CatalogResponse update(Integer id, CatalogRequest catalogRequest) {
        Catalog catalog = mapRequestToPoJo(catalogRequest);
        catalog.setId(id);
        Catalog catalogUpdate = catalogRepo.save(catalog);
        CatalogResponse catalogResponse = mapPoJoToResponse(catalogUpdate);
        return catalogResponse;
    }


    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            Catalog catalogDelete = catalogRepo.findById(id).get();
            catalogDelete.setStatus(0);
            catalogRepo.save(catalogDelete);
            return ResponseEntity.ok().body(Message.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @Override
    public List<Catalog> findAll() {
        List<Catalog> responses = catalogRepo.findAll();
        return responses;
    }

    @Override
    public List<CatalogResponse> getAllForClient() {
        List<CatalogResponse> responses = catalogRepo.findAll().stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public Catalog findById(Integer id) {
        return catalogRepo.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Catalog> page = catalogRepo.findByNameContaining(name, pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public Catalog mapRequestToPoJo(CatalogRequest catalogRequest) {
        Catalog catalog = new Catalog();
        catalog.setName(catalogRequest.getName());
        catalog.setStatus(catalogRequest.getStatus());
        return catalog;
    }

    @Override
    public CatalogResponse mapPoJoToResponse(Catalog catalog) {
        CatalogResponse response = new CatalogResponse();
        response.setId(catalog.getId());
        response.setName(catalog.getName());
        response.setStatus(catalog.getStatus());
        response.setTotalProductInCatalog(catalog.getProductList().size());
        return response;
    }

    @Override
    public List<CatalogResponse> getListFeatured() {
        List<CatalogResponse> responses = catalogRepo.findAll().stream()
                .sorted(Comparator.comparingInt(catalog -> catalog.getProductList().size()))
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        List<CatalogResponse> result = responses.stream()
                .skip(Math.max(0, responses.size() - 6))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<CatalogResponse> getFeatureCatalogForScreen2(LocalDateTime startDate, LocalDateTime endDate, int size) {
        List<ProductResponse> productResponseList = productService.getFeatureProduct(startDate, endDate, size);
        List<Product> productList = new ArrayList<>();
        for (ProductResponse pro : productResponseList) {
            productList.add(productService.findById(pro.getId()));
        }
        Set<Catalog> catalogList = catalogRepo.findByProductListIn(productList);
        List<CatalogResponse> responses = catalogList.stream()
                .map(this::mapPoJoToResponse).collect(Collectors.toList());
        return responses;
    }

    @Override
    public List<CatalogResponse> countTypeOfProduct() {
        Map<Integer, Integer> newMap = new HashMap<>();
        List<Catalog> listCatalog = findAll();
        for (Catalog cat : listCatalog) {
            int quantity = productService.countByCatalog_Id(cat.getId());
            newMap.put(cat.getId(), quantity);
        }
        List<CatalogResponse> responses = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : newMap.entrySet()) {
            CatalogResponse cat = mapPoJoToResponse(findById(entry.getKey()));
            cat.setQuantityTypeOfProduct(entry.getValue());
            responses.add(cat);
        }
        return responses;
    }

    @Override
    public CatalogResponse finByIdResponse(int id) {
        return mapPoJoToResponse(findById(id));
    }

    @Override
    public List<CatalogResponse> getTop5CatalogsHaveTheBestRevenue(LocalDateTime startTime, LocalDateTime endTime) {
        List<CartDetail> cartDetailList = cartDetailRepository.get_real_revenue_product_des(startTime, endTime);
        List<Product> productList = productService.findByCartDetailListIn(cartDetailList);
        Set<Catalog> catalogList = catalogRepo.findByProductListIn(productList);
        List<CatalogResponse> responses = catalogList.stream()
                .skip(catalogList.size() - 5)
                .limit(5).map(this::mapPoJoToResponse).collect(Collectors.toList());
        return responses;
    }


}
