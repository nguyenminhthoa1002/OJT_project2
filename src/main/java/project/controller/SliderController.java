package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.SliderService;
import project.model.dto.request.SliderRequest;
import project.model.dto.response.SliderResponse;
import project.model.entity.Slider;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("https://localhost8080")
@RequestMapping("/api/v1/slider")
@AllArgsConstructor
public class SliderController {
    private SliderService sliderService;

    @GetMapping
    public ResponseEntity<?> getAllSlider() {
        try {
            List<Slider> responses = sliderService.findAll();
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/{sliderId}")
    public ResponseEntity<?> findSliderById(@PathVariable("sliderId") int sliderId) {
        try {
            Slider slider = sliderService.findById(sliderId);
            return new ResponseEntity<>(slider, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PostMapping
    public ResponseEntity<?> createSlider(@RequestBody SliderRequest sliderRequest) {
        try {
            SliderResponse sliderResponse = sliderService.saveOrUpdate(sliderRequest);
            return new ResponseEntity<>(sliderResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/{sliderId}")
    public ResponseEntity<?> updateSlider(@RequestBody SliderRequest sliderRequest, @PathVariable("sliderId") int sliderId) {
        try {
            SliderResponse sliderResponse = sliderService.update(sliderId,sliderRequest);
            return new ResponseEntity<>(sliderResponse,HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @DeleteMapping("/{sliderId}")
    public ResponseEntity<?> deleteSlider(@PathVariable("sliderId") int sliderId) {
        return sliderService.delete(sliderId);
    }

    @GetMapping("getPaging")
    public ResponseEntity<?> pageAndSortSlider(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object> result = sliderService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("search")
    public ResponseEntity<?> searchAndSortAndPaging(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable = Utility.sort_order(headers);
            Map<String,Object> result = sliderService.findByName(headers.get("searchName"),pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("getNewestSlider")
    public ResponseEntity<?> getFeatureSlider(){
        try {
            List<SliderResponse> sliderResponseList = sliderService.getAllForClient();
            List<SliderResponse> responses = sliderResponseList.stream()
                    .skip(Math.max(0, sliderResponseList.size()-6))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
