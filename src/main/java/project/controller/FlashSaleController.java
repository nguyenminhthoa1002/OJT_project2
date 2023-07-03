package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.FlashSaleService;
import project.model.dto.request.FlashSaleRequest;
import project.model.dto.response.FlashSaleResponse;
import project.model.shopMess.Message;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/flashSale")
@AllArgsConstructor
public class FlashSaleController {
    private FlashSaleService flashSaleService;
    @PostMapping
    public ResponseEntity<?>addNewFlashSale(@RequestBody FlashSaleRequest request){
        try {
            FlashSaleResponse response=flashSaleService.saveOrUpdate(request);
            if (response!=null){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(Message.ERROR_EXISTED_TIME, HttpStatus.OK);
            }

        }catch (Exception e){
            return new ResponseEntity<>(Message.ERROR_400, HttpStatus.OK);
        }
    }
    @GetMapping("/get_on_sale")
    public ResponseEntity<?> getAllForClient(){
        try {
            List<FlashSaleResponse> responses=flashSaleService.getAllForClient();
            return new ResponseEntity<>( responses,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(Message.ERROR_400, HttpStatus.OK);
        }
    }
}
