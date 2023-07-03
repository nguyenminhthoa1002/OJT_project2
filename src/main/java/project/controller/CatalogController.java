package project.controller;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CatalogService;
import project.model.dto.request.CatalogRequest;
import project.model.dto.request.ProductFeatureRequest;
import project.model.dto.response.CatalogResponse;
import project.model.utility.Utility;
import project.model.shopMess.Message;

import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/catalog")
@AllArgsConstructor
public class CatalogController {
    private CatalogService catalogService;
    @GetMapping("/get_paging_and_sort")
    public ResponseEntity<?> get_paging_and_sort(@RequestParam Map<String,String> headers) {
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String, Object> result = catalogService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/get_feature_catalog")
    public ResponseEntity<?> getFeatureCatalog(){
        try {
            List<CatalogResponse> result= catalogService.getListFeatured();
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllClient")
    public ResponseEntity<?>getAll_Client(){
        List<CatalogResponse>listCat=catalogService.getAllForClient();
        return new ResponseEntity<>(listCat,HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<?>getById(@PathVariable("id")int id){
        CatalogResponse catalogResponse= catalogService.finByIdResponse(id);
        return new ResponseEntity<>(catalogResponse,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?>creatNewCatalog(@RequestBody CatalogRequest request){
        try {
            CatalogResponse result= catalogService.saveOrUpdate(request);
            return  new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?>update(@PathVariable("id")int id,@RequestBody CatalogRequest catalogRequest){
        try {
            CatalogResponse catalogResponse=catalogService.update(id,catalogRequest);
            return new ResponseEntity<>(catalogResponse,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delele(@PathVariable("id")int id){
        try {
            catalogService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?>search(@RequestParam Map<String,String> headers){
       try {
           Pageable pageable=Utility.sort_order(headers);
           Map<String,Object>result=catalogService.findByName(headers.get("name"),pageable);
           return new ResponseEntity<>(result,HttpStatus.OK);

       }catch (Exception e){
           return ResponseEntity.badRequest().body(Message.ERROR_400);
       }
    }
    @GetMapping("/get_feature_catalog_for_screen_2")
    public ResponseEntity<?> getFeatureCatalogForScreen2(@RequestBody ProductFeatureRequest productFeatureRequest){
        try {
            List<CatalogResponse> result= catalogService.getFeatureCatalogForScreen2(productFeatureRequest.getStartDate(),productFeatureRequest.getEndDate(), productFeatureRequest.getSize());
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("countTypesOfProduct")
    public ResponseEntity<?> countTypeOfProductInCatalog(){
        try {
            List<CatalogResponse> result = catalogService.countTypeOfProduct();
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
