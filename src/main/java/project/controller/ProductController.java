package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.ProductService;
import project.model.dto.request.FilterProductRequest;
import project.model.dto.request.ProductFeatureRequest;
import project.model.dto.request.ProductRequest;
import project.model.dto.response.ProductResponse;
import project.model.entity.Product;
import project.model.shopMess.Message;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.model.utility.Utility;


@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping("getFeatureProduct")
    public ResponseEntity<?> getFeatureSlider(@RequestBody ProductFeatureRequest productFeatureRequest) {
        try {
            List<ProductResponse> responses = productService.getFeatureProduct(productFeatureRequest.getStartDate(), productFeatureRequest.getEndDate(), productFeatureRequest.getSize());
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/top_new_product")
    public ResponseEntity<?> topNewProduct() {
        try {
            List<ProductResponse> responses = productService.topNewProduct();
            if (responses.isEmpty()) {
                return ResponseEntity.badRequest().body(Message.ERROR_NULL);
            } else {
                return new ResponseEntity<>(responses, HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("top_rated_product")
    public ResponseEntity<?> topRatedProduct(@RequestBody ProductFeatureRequest productFeatureRequest) {
        try {
            List<ProductResponse> responses = productService.getTopRatedProduct(productFeatureRequest.getStartDate(), productFeatureRequest.getEndDate(), productFeatureRequest.getSize());
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("search_sort_newest_product")
    public ResponseEntity<?> searchAndSortNewestProductByName(@RequestParam Map<String, String> headers) {
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String, Object> result = productService.findByName(headers.get("searchName"), pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("filter_product_by_Price_Location_Rating")
    public ResponseEntity<?> filterProductByPriceLocationAndStar(@RequestBody FilterProductRequest filterProductRequest) {
        try {
            List<ProductResponse> responses = productService.filterProductByPriceLocationStar(
                    filterProductRequest.getLocationId(),
                    filterProductRequest.getMax(),
                    filterProductRequest.getMin(),
                    filterProductRequest.getStarPoint()
            );
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("getProductResponseById")
    public ResponseEntity<?> getProductResponseById(@RequestParam int productId){
        try {
            ProductResponse response = productService.getProductResponseById(productId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/get_paging_and_sort")
    public ResponseEntity<?> get_paging_and_sort(@RequestParam Map<String,String> headers){
        try{
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object> result = productService.getPagingAndSort(pageable);
            return  new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?>getById(@PathVariable("id")int id){
        try{
            Product product = productService.findById(id);
            ProductResponse productResponse = productService.mapPoJoToResponse(product);
            return new ResponseEntity<>(productResponse,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }

    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(@RequestParam Map<String,String> headers){
        try{
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object>result=productService.findByName(headers.get("name"),pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable("id")int id){
        try{
            productService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?>update(@PathVariable("id")int id, @RequestBody ProductRequest productRequest){
        try{
            ProductResponse result = productService.update(id,productRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PostMapping
    public ResponseEntity<?> creatNewProduct(@RequestBody ProductRequest productRequest){
        try {
            ProductResponse result= productService.saveOrUpdate(productRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
