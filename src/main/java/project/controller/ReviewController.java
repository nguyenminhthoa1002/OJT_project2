package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.ReviewService;
import project.model.dto.request.ReviewRequest;
import project.model.dto.response.BlogResponse;
import project.model.dto.response.ReviewResponse;
import project.model.entity.Review;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.ProductRepository;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/review")
@AllArgsConstructor
public class ReviewController {
    private ReviewService reviewService;
    private final ProductRepository productRepository;

    @PostMapping("add_new_review")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> addNewReview(@RequestBody ReviewRequest reviewRequest){
        try {
            ReviewResponse response = reviewService.addNewReview(reviewRequest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/{id}")
    public ReviewResponse getReviewResponseById(@PathVariable int id){
        return reviewService.getReviewResponseById(id);
    }

    @GetMapping("/searchByProductId")
    public ResponseEntity<?>search_Sort_paging_ByProduct(@RequestParam Map<String,String> headers){
        try {
           int productId = Integer.parseInt(headers.get("productId"));
            Pageable pageable= Utility.sort_order(headers);
           Map<String,Object>result=reviewService.findReviewByProductId(productId,pageable);
            return new  ResponseEntity(result,HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/searchUserId")
    public ResponseEntity<?>search_Sort_paging_ByUser(@RequestParam Map<String,String> headers){
        try {
            int userId = Integer.parseInt(headers.get("userId"));
            Pageable pageable= Utility.sort_order(headers);
            Map<String,Object>result=reviewService.findReviewByUsersUserId(userId,pageable);
            return new  ResponseEntity(result,HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping
    public ResponseEntity<?>getAll_paging_sort_review(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable= Utility.sort_order(headers);
            Map<String,Object> result = reviewService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?>update(@PathVariable("id")int id,@RequestBody ReviewRequest reviewRequest){
        try {
            ReviewResponse resul=reviewService.update(id,reviewRequest);
            return new ResponseEntity<>(resul,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PutMapping("/updateAdmin/{id}")
    public ResponseEntity<?>updateAdmin(@PathVariable("id")int id,@RequestBody ReviewRequest reviewRequest){
        try {
            ReviewResponse resul=reviewService.updateAdmin(id,reviewRequest);
            return new ResponseEntity<>(resul,HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @DeleteMapping("delete/{id}")
    public ResponseEntity<?>delete(@PathVariable("id")int id){
        try {
            reviewService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }


}
