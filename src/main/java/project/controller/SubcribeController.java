package project.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.SubcribeService;
import project.model.dto.response.SubcribeResponse;
import project.model.shopMess.Message;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/subcribe")
@AllArgsConstructor
public class SubcribeController {
    private SubcribeService subcribeService;
    @PostMapping
    public ResponseEntity<?> getSubcribe(@RequestParam String email){
        try {
            SubcribeResponse sr = subcribeService.getSubcribe(email);
            return new ResponseEntity<>(sr, HttpStatus.OK);
        } catch (Exception ex){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

}
