package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.BrandService;
import project.model.dto.request.BrandRequest;
import project.model.dto.request.ProductFeatureRequest;
import project.model.dto.response.BrandResponse;
import project.model.entity.Brand;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/brand")
@AllArgsConstructor
public class BrandController {
    private BrandService brandService;
    @GetMapping
    public List<Brand> getAll(){
        return brandService.findAll();
    }
    @GetMapping("/get_feature_brand")
    public ResponseEntity<?> getFeatureBrand(@RequestBody ProductFeatureRequest productFeatureRequest){
        try {
            List<BrandResponse> responses = brandService.getFeatureBrand(productFeatureRequest.getStartDate(), productFeatureRequest.getEndDate(), productFeatureRequest.getSize());
            return new ResponseEntity<>(responses,HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?>getById(@PathVariable("id") int id){
        Brand brand = brandService.findById(id);
        return new ResponseEntity<>(brand,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?>create(@RequestBody BrandRequest brandRequest){
        try{
            BrandResponse result = brandService.saveOrUpdate(brandRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?>update(@PathVariable("id")int id,@RequestBody BrandRequest brandRequest){
        try{
            BrandResponse result = brandService.update(id,brandRequest);
            return  new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable("id")int id){
        try{
            brandService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?>searchByName_sort_paping(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object>result =brandService.findByName(headers.get("name"),pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/sort_paping")
    public ResponseEntity<?>sort_Paging(@RequestParam Map<String,String> headers){
        try{
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object>result=brandService.getPagingAndSort(pageable);
            return  new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}


