package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.TagService;
import project.model.dto.request.TagRequest;
import project.model.dto.response.TagResponse;
import project.model.entity.Tags;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tags> getAll() {
        return tagService.findAll();
    }

    @GetMapping("/getAll_client")
    public ResponseEntity<?> getAll_Client() {
        List<TagResponse> tagResponsesList = tagService.getAllForClient();
        return new ResponseEntity<>(tagResponsesList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Tags getById(@PathVariable("id") int id) {
        return tagService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody TagRequest tagRequest) {
        try {
            TagResponse tagResponse = tagService.saveOrUpdate(tagRequest);
            return new ResponseEntity<>(tagResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody TagRequest tagRequest) {
        try {
            TagResponse tagResponse = tagService.update(id, tagRequest);
            return new ResponseEntity<>(tagResponse, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        try {
            tagService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam Map<String, String> header) {
        try {
            Pageable pageable = Utility.sort_order(header);
            Map<String, Object> result = tagService.findByName(header.get("name"), pageable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }

    @GetMapping("/paging")
    public ResponseEntity<?> paging(@RequestParam Map<String, String> header) {
        try {
            Pageable pageable=Utility.sort_order(header);
            Map<String,Object>result =tagService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}