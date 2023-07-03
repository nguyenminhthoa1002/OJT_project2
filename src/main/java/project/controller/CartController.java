package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CartDetailService;
import project.bussiness.service.CartService;
import project.model.dto.request.CartConfirmRequest;
import project.model.dto.request.CartDetailRequest;
import project.model.dto.response.CartPendingResponse;
import project.model.dto.response.CartResponse;
import project.model.dto.response.ProductReportByBrand;
import project.model.dto.response.ProductReportByCatalog;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/cart")
@AllArgsConstructor
public class CartController {
    private CartService cartService;
    private CartDetailService detailService;
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> addToCart(@RequestBody CartDetailRequest cartDetailRequest, @RequestParam String action){
        return cartService.addToCart(cartDetailRequest, action);
    }
    @PutMapping("/delete-cartDetail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> deleteCartDetail(@RequestParam Integer cartDetailId){
        return detailService.delete(cartDetailId);
    }
    @GetMapping("/get_cart_pending_for_user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?>getCartPending(){
        try {
            CartPendingResponse response= cartService.showCartPending();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping ("/get_paging_and_sort")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') ")
    public ResponseEntity<?> getAllCartForAdmin(@RequestParam Map<String,String> headers){
        try{
            Pageable pageable= Utility.sort_order(headers);
            Map<String, Object> result = cartService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/change_status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') ")
    public ResponseEntity<?>confirmNextStatus(@RequestParam Integer cartId,Integer status ){
        return cartService.changeStatus(cartId, status);
    }
    @GetMapping("/get_all_history")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR')or hasRole('USER') ")
    public ResponseEntity<?> getAllByUserId(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable=Utility.sort_order(headers);
            Map<String,Object> result=cartService.getAllForClient(pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/pre_check_out")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> pre_checkout(@RequestParam int couponId) {
        try {
            CartResponse result = cartService.showCartCheckout(couponId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("check_out")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> checkout(@RequestBody CartConfirmRequest cartConfirmRequest) {
        try {
            return cartService.checkout(cartConfirmRequest);
        }catch (Exception e){
            return new ResponseEntity(Message.ERROR_400,HttpStatus.BAD_REQUEST);
        }
    }
}
