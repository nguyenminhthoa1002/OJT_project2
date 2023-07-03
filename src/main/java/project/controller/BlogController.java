package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.BlogService;
import project.model.dto.request.BlogRequest;
import project.model.dto.response.BlogResponse;
import project.model.entity.Blog;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/blog")
@AllArgsConstructor
public class BlogController {
    private BlogService blogService;
    @GetMapping("/top_new_blog")
    public ResponseEntity<?> topNewBlog(){
        try {
            List<BlogResponse> responses=blogService.getTopNew();
            if (responses.isEmpty()){
                return new ResponseEntity<>(Message.ERROR_NULL, HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(responses,HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(Message.ERROR_400, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("byId/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int blogId){
        try {
            BlogResponse blog = blogService.getBlogResponseForClient(blogId);
            return new ResponseEntity<>(blog, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.accepted().body(Message.ERROR_400);
        }
    }

    @GetMapping
    public ResponseEntity<?>getAll_blog(){
        List<BlogResponse>listblog = blogService.getAllForClient();
        return new ResponseEntity<>(listblog,HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?>getById_blog(@PathVariable("id")int id){
        Blog blog =blogService.findById(id);
        return new ResponseEntity<>(blog,HttpStatus.OK);

    }
    @GetMapping("/getAll_paging_sort_blog")
    public ResponseEntity<?>getAll_paging_sort_blog(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable= Utility.sort_order(headers);
            Map<String,Object> result = blogService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PostMapping()
    public ResponseEntity<?>create_blog(@RequestBody BlogRequest blogRequest){
        try {
            BlogResponse result = blogService.saveOrUpdate(blogRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PutMapping("/update_blog/{id}")
    public ResponseEntity<?>updaste_blog(@PathVariable("id")int id,@RequestBody BlogRequest blogRequest){
        try {
           BlogResponse result = blogService.update(id,blogRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @DeleteMapping("/delete_blog/{id}")
    public ResponseEntity<?>delete_blog(@PathVariable("id")int id){
        try {
            blogService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/search_blog")
    public ResponseEntity<?>search_Sort_paging(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable=Utility.sort_order(headers);
            Map<String,Object>result = blogService.findByName(headers.get("name"),pageable);
            return new  ResponseEntity(result,HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/get_related_blog")
    public ResponseEntity<?>get_Related_Blog(@RequestParam int catId){
        try {
            List<BlogResponse> responses = blogService.getRelatedBlog(catId);
            return new ResponseEntity<>(responses,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/search_blog_by_catalog_and_tag")
    public ResponseEntity<?>search_By_Catalog_And_Tag(@RequestBody BlogRequest blogRequest ){
        try {
            List<BlogResponse> blogResponseList = blogService.searchByCatalogAndTag(blogRequest.getCatalogId(), blogRequest.getTagId());
            return new ResponseEntity<>(blogResponseList,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

}
