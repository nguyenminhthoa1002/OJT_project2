package project.bussiness.service;

import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.entity.CartDetail;
import project.model.entity.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProductService extends RootService<Product,Integer, ProductRequest, ProductResponse> {
    List<ProductResponse> topNewProduct();
    List<ProductResponse> getFeatureProduct(LocalDateTime startDate, LocalDateTime endDate, int size);
    List<Product> findByCartDetailListIn(List<CartDetail> listCartDetail);
    List<ProductResponse> getTopRatedProduct(LocalDateTime startDate, LocalDateTime endDate, int size);
    int countByCatalog_Id(int catalogId);
    List<ProductResponse> filterProductByPriceLocationStar(List<Integer> listLocationId, float max, float min, List<Integer> starPoint);
    ProductResponse getProductResponseById(int id);
    List<ProductResponse> get_top_revenue(LocalDateTime startDate, LocalDateTime endDate, int size);
    Map<String,Integer> likeProduct();
}
