package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CommentService;
import project.bussiness.service.UserService;
import project.model.dto.request.CatalogRequest;
import project.model.dto.request.CommentRequest;
import project.model.dto.response.CatalogOfBlogReponse;
import project.model.dto.response.CatalogResponse;
import project.model.dto.response.CommentResponse;
import project.model.entity.CommentBlog;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.CommentRepository;

import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/comment")
@AllArgsConstructor
public class CommentController {
    private CommentService commentService;
    private UserService userService;
    @PostMapping
    public ResponseEntity<?> creatNewComment(@RequestBody CommentRequest request){
        try {
            CommentResponse result= commentService.saveOrUpdate(request);
            return  new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?>getById(@PathVariable("id")int id){
        CommentResponse commentResponse = commentService.finByIdResponse(id);
        return new ResponseEntity<>(commentResponse,HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?>update(@PathVariable("id")int id,@RequestBody CommentRequest commentRequest){
        try {
            CommentResponse result = commentService.update(id,commentRequest);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable("id")int id){
        try {
            commentService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?>searchByName_sort_paging(@RequestParam Map<String,String> headers){
        try{
            int blogId = Integer.parseInt(headers.get("blogId"));
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object>result=commentService.searchByBlogId(pageable,blogId);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/sort_paging")
    public ResponseEntity<?>sort_Paging(@RequestParam Map<String,String> headers){
        try{
            Pageable pageable =Utility.sort_order(headers);
            Map<String,Object>result=commentService.findByName(headers.get("name"),pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
