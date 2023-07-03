package project.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/")
public class TestDocker {
    @GetMapping()
    public String test(){
        return "IT's work";
    }
}
