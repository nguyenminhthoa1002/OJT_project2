package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.WishService;
import project.model.dto.request.WishRequest;
import project.model.dto.response.WishResponse;
import project.model.entity.Wish;
import project.model.shopMess.Message;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/wish")
@AllArgsConstructor
public class WishController {
    private WishService wishService;
    @PostMapping("/add_wish_list")
    public ResponseEntity<?> addWishList(@RequestBody WishRequest rq){
        WishResponse response;
        Wish wish = wishService.findByUsers_UserIdAndProduct_Id(rq.getUserId(),rq.getProductId());
        try {
            if (wish!=null){
                response=wishService.update(wish.getId(),rq);
            }else {
                response= wishService.saveOrUpdate(rq);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

}
