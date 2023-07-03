package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CatalogOfBlogService;
import project.model.dto.request.CatalogOfBlogRequest;
import project.model.dto.response.CatalogOfBlogReponse;
import project.model.entity.CatalogOfBlog;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/catalogBlog")
@AllArgsConstructor
public class CatalogBlogController {
    @Autowired
    private CatalogOfBlogService catalogOfBlogService;
    @GetMapping
    public List<CatalogOfBlog> getAll(){
        return catalogOfBlogService.findAll();
    }
    @GetMapping("/getALL_client")
    public ResponseEntity<?> getALl_Client(){
        List<CatalogOfBlogReponse>catalogOfBlogReponseList=catalogOfBlogService.getAllForClient();
        return new ResponseEntity<>(catalogOfBlogReponseList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?>getById(@PathVariable("id")int id){
        CatalogOfBlog catalogOfBlog =catalogOfBlogService.findById(id);
        return new ResponseEntity<>(catalogOfBlog,HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?>create(@RequestBody CatalogOfBlogRequest catalogOfBlogRequest){
        try {
            CatalogOfBlogReponse result = catalogOfBlogService.saveOrUpdate(catalogOfBlogRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?>update(@PathVariable("id")int id,@RequestBody CatalogOfBlogRequest catalogOfBlogRequest){
        try {
            CatalogOfBlogReponse result = catalogOfBlogService.update(id,catalogOfBlogRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);

        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable("id")int id){
        try {
            catalogOfBlogService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?>searchByName_sort_paging(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object>result =catalogOfBlogService.findByName(headers.get("name"),pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/sort_paging")
    public ResponseEntity<?>sort_Paging(@RequestBody Map<String,String> headers){
        try {
            Pageable pageable =Utility.sort_order(headers);
            Map<String,Object>rusult=catalogOfBlogService.getPagingAndSort(pageable);
            return new ResponseEntity<>(rusult,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

}
