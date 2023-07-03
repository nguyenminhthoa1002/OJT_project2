package project.bussiness.serviceImpl;

import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.ProductService;
import project.bussiness.service.ReviewService;
import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.dto.response.ReviewResponse;
import project.model.entity.*;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductImpl implements ProductService {
    private CartRepository cartRepository;
    private CartDetailRepository cartDetailRepository;
    private ProductRepository productRepo;
    private CatalogRepository catalogRepo;
    private BrandRepository brandRepo;
    private LocationRepository locationRepository;
    private ReviewService reviewService;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<ProductResponse> page = productRepo.findAll(pageable).map(this::mapPoJoToResponse);
        Map<String,Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public ProductResponse saveOrUpdate(ProductRequest productRequest) {
        Product product = mapRequestToPoJo(productRequest);
        Product productNew = productRepo.save(product);
        ProductResponse productResponse = mapPoJoToResponse(productNew);
        return productResponse;
    }

    @Override
    public ProductResponse update(Integer id, ProductRequest productRequest) {
        Product product = mapRequestToPoJo(productRequest);
        product.setId(id);
        Product productUpdate = productRepo.save(product);
        ProductResponse productResponse = mapPoJoToResponse(productUpdate);
        return productResponse;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            Product productDelete = productRepo.findById(id).get();
            productDelete.setStatus(0);
            return ResponseEntity.ok().body(Message.SUCCESS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }

    }

    @Override
    public List<Product> findAll() {
        for (Product pd : productRepo.findAll()) {
            if (pd.getProductQuantity()==0) {
                pd.setStatus(0);
                productRepo.save(pd);
            }
        }
        return productRepo.findAll();
    }

    @Override
    public List<ProductResponse> getAllForClient() {
        List<ProductResponse> productResponseList = productRepo.findAll().stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        return productResponseList;
    }

    @Override
    public Product findById(Integer id) {
        return productRepo.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Product> page = productRepo.findByNameContaining(name, pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        result.get(page.getTotalElements());
        return result;
    }

    @Override
    public List<ProductResponse> getFeatureProduct(LocalDateTime startDate, LocalDateTime endDate, int size) {
        List<Cart> listCart = cartRepository.findByCreatDateBetween(startDate, endDate);
        List<Cart> listCartFilter = listCart.stream().filter(cart -> cart.getStatus() == 1)
                .collect(Collectors.toList());
        List<CartDetail> listCartDetail = cartDetailRepository.findByCartIn(listCartFilter);
        Map<Integer, Integer> maxMap = new HashMap<>();
        for (int i = 0; i < listCartDetail.size(); i++) {
            int quantity = listCartDetail.get(i).getQuantity();
            int key = listCartDetail.get(i).getProduct().getId();
            if (maxMap.containsKey(key)) {
                int value = maxMap.get(key);
                maxMap.put(key, value + quantity);
            } else {
                maxMap.put(key, quantity);
            }
        }

        Map<Integer, Integer> result = maxMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .skip(maxMap.size() - size)
                .limit(size)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));

        List<Product> listProduct = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            listProduct.add(findById(entry.getKey()));
        }

        List<ProductResponse> responses = listProduct.stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());

        return responses;
    }

    @Override
    public List<Product> findByCartDetailListIn(List<CartDetail> listCartDetail) {
        return productRepo.findByCartDetailListIn(listCartDetail);
    }

    @Override
    public List<ProductResponse> getTopRatedProduct(LocalDateTime startDate, LocalDateTime endDate, int size) {
        List<Cart> listCart = cartRepository.findByCreatDateBetween(startDate, endDate);
        List<Cart> listCartFilter = listCart.stream().filter(cart -> cart.getStatus() == 1)
                .collect(Collectors.toList());
        List<CartDetail> listCartDetail = cartDetailRepository.findByCartIn(listCartFilter);
        List<Product> listProduct = findByCartDetailListIn(listCartDetail);
        List<ProductResponse> listProductRes = listProduct.stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        List<ProductResponse> responses = listProductRes.stream()
                .sorted(Comparator.comparingDouble(response -> response.getStarPoint()))
                .skip(listProductRes.size() - size)
                .limit(size)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public int countByCatalog_Id(int catalogId) {
        return productRepo.countByCatalog_Id(catalogId);
    }

    @Override
    public List<ProductResponse> filterProductByPriceLocationStar(List<Integer> listLocationId, float max, float min, List<Integer> starPoint) {
        List<Location> locationList = new ArrayList<>();
        if (listLocationId.size() == 0) {
            locationList = locationRepository.findAll();
        }
        for (Integer locationId : listLocationId) {
            locationList.add(locationRepository.findById(locationId).get());
        }
        List<Product> listProduct = productRepo.findByLocationIn(locationList);
        List<ProductResponse> listProductResponse = new ArrayList<>();
        if (max == 0 || min == 0) {
            listProductResponse = listProduct.stream()
                    .map(this::mapPoJoToResponse)
                    .sorted(Comparator.comparing(ProductResponse::getCreatDate).reversed())
                    .collect(Collectors.toList());
        } else {
            listProductResponse = listProduct.stream()
                    .filter(product -> product.getExportPrice() >= min && product.getExportPrice() <= max)
                    .map(this::mapPoJoToResponse)
                    .sorted(Comparator.comparing(ProductResponse::getCreatDate).reversed())
                    .collect(Collectors.toList());
        }
        if (starPoint.size() == 0) {
            return listProductResponse;
        } else {
            Collections.sort(starPoint);
            List<ProductResponse> responses = listProductResponse.stream()
                    .filter(response -> response.getStarPoint() >= starPoint.get(0) && response.getStarPoint() <= starPoint.get(starPoint.size() - 1))
                    .sorted(Comparator.comparing(ProductResponse::getCreatDate).reversed())
                    .collect(Collectors.toList());
            return responses;
        }

    }

    @Override
    public ProductResponse getProductResponseById(int id) {
        return mapPoJoToResponse(findById(id));
    }

    @Override
    public List<ProductResponse> get_top_revenue(LocalDateTime startDate, LocalDateTime endDate, int size) {
        return null;
    }


    @Override
    public Product mapRequestToPoJo(ProductRequest productRequest) {
        Product product = new Product();
        if (productRequest.getStatus() > 0) {
            product.setStatus(productRequest.getStatus());
        } else {
            product.setStatus(1);
        }
        product.setName(productRequest.getName());
        product.setCreatDate(productRequest.getCreatDate());
        product.setDiscount(productRequest.getDiscount());
        product.setExportPrice(productRequest.getExportPrice());
        product.setImportPrice(productRequest.getImportPrice());
        product.setProductDescriptions(productRequest.getProductDescriptions());
        product.setProductImg(productRequest.getProductImg());
        product.setProductQuantity(productRequest.getProductQuantity());
        product.setTitle(productRequest.getTitle());
        product.setCatalog(catalogRepo.findById(productRequest.getCatalogId()).get());
        product.setBrand(brandRepo.findById(productRequest.getBrandId()).get());
        return product;
    }

    @Override
    public ProductResponse mapPoJoToResponse(Product pro) {
        ProductResponse rp = new ProductResponse();
        rp.setId(pro.getId());
        rp.setName(pro.getName());
        rp.setStatus(pro.getStatus());
        rp.setCreatDate(pro.getCreatDate());
        rp.setDiscount(pro.getDiscount());
        rp.setExportPrice(pro.getExportPrice());
        rp.setImportPrice(pro.getImportPrice());
        rp.setProductDescriptions(pro.getProductDescriptions());
        rp.setProductImg(pro.getProductImg());
        rp.setProductQuantity(pro.getProductQuantity());
        rp.setTitle(pro.getTitle());
        rp.setBrandName(pro.getBrand().getName());
        rp.setCatalogName(pro.getCatalog().getName());
        List<ReviewResponse> listReviewRes = new ArrayList<>();
        for (Review rv : pro.getReviewList()) {
            listReviewRes.add(reviewService.mapPoJoToResponse(rv));
        }
        rp.setReviewList(listReviewRes);
        rp.setSubImgList(pro.getSubImgList());
        List<Integer> list = new ArrayList<>();
        for (Review rw : pro.getReviewList()) {
            list.add(rw.getStarPoint());
        }
        double average = list.stream().mapToDouble(num -> num).average().orElse(0.0);
        rp.setStarPoint(average);
        rp.setCountAllReview(pro.getReviewList().size());
        return rp;
    }

    @Override
    public List<ProductResponse> topNewProduct() {
        List<ProductResponse> responses = productRepo.findAll().stream().sorted(Comparator.comparing(Product::getCreatDate)).map(this::mapPoJoToResponse).collect(Collectors.toList());
        List<ProductResponse> result = responses.stream()
                .skip(Math.max(0, responses.size()) - 3)
                .filter(Objects::nonNull).collect(Collectors.toList());
        return result;
    }
    @Override
    public Map<String, Integer> likeProduct() {
        List<Integer> list = productRepo.likeProduct();
        List<Product> productList = new ArrayList<>();
        for (Integer id:list) {
            Product product = productRepo.findById(id).get();
            productList.add(product);
        }

        Map<String, Integer> data = new HashMap<>();
        for (Product product:productList) {
            String key = product.getName();
            if (data.containsKey(key)) {
                int newQuantity = data.get(key) +1;
                data.put(product.getName(),newQuantity);
            } else {
                data.put(product.getName(),1);
            }
        }
        return data;
    }

}
