package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.CouponService;
import project.model.dto.request.CouponRequest;
import project.model.dto.request.SliderRequest;
import project.model.dto.response.CouponResponse;
import project.model.dto.response.SliderResponse;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/coupon")
@AllArgsConstructor
public class CouponController {
    private CouponService couponService;
        @GetMapping("/get-for-user")
    public ResponseEntity<?> getByUserId(){
        try {
            List<CouponResponse> responseList=couponService.getAllForClient();
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }catch (Exception exception){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("getPaging")
    public ResponseEntity<?> pageAndSortCoupon(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object> result = couponService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("search")
    public ResponseEntity<?> searchAndSortAndPaging(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object> result = couponService.findByName(headers.get("searchName"),pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody CouponRequest couponRequest) {
        try {
            CouponResponse couponResponse = couponService.saveOrUpdate(couponRequest);
            return new ResponseEntity<>(couponResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/{couponId}")
    public ResponseEntity<?> updateCoupon(@RequestBody CouponRequest couponRequest, @PathVariable("couponId") int couponId) {
        try {
            CouponResponse couponResponse = couponService.update(couponId,couponRequest);
            return new ResponseEntity<>(couponResponse,HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @DeleteMapping("/{couponId}")
    public ResponseEntity<?> deleteSlider(@PathVariable("couponId") int couponId) {
        return couponService.delete(couponId);
    }
}
