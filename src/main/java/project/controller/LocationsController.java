package project.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.bussiness.service.LocationService;
import project.model.dto.request.LocationRequest;
import project.model.dto.response.LocationResponse;
import project.model.entity.Location;
import project.model.shopMess.Message;
import project.model.utility.Utility;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("/api/v1/location")
public class LocationsController {
    @Autowired
    private LocationService locationService;
//    @GetMapping
//    public List<Location>getAll(){
//        List<Location> list=locationService.findAll();
//        return list;
//    }
    @GetMapping("{id}")
    public Location findById(@PathVariable("id")int id){
        Location location=locationService.findById(id);
        return location;
    }
    @PostMapping("/create")
    public ResponseEntity<?>create(@RequestBody LocationRequest locationRequest){
        try {
            LocationResponse locationResponse=locationService.saveOrUpdate(locationRequest);
            return new ResponseEntity<>(locationResponse, HttpStatus.OK);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?>update(@PathVariable("id")int id,@RequestBody LocationRequest locationRequest){
       try {
           LocationResponse locationResponse=locationService.update(id,locationRequest);
           return new ResponseEntity<>(locationResponse,HttpStatus.OK);

       }catch (Exception e){
           return ResponseEntity.badRequest().body(Message.ERROR_400);
       }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>delete(@PathVariable("id")int id){
        try {
            locationService.delete(id);
            return ResponseEntity.ok().body(Message.SUCCESS);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/getAll_paging_sort_location")
    public ResponseEntity<?>getAll_paging_sort_blog(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable= Utility.sort_order(headers);
            Map<String,Object> result = locationService.getPagingAndSort(pageable);
            return new ResponseEntity<>(result,HttpStatus.OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @GetMapping("/search")
    public ResponseEntity<?>search_Sort_paging(@RequestParam Map<String,String> headers){
        try {
            Pageable pageable=Utility.sort_order(headers);
            Map<String,Object>result = locationService.findByName(headers.get("name"),pageable);
            return new  ResponseEntity(result,HttpStatus.OK);

        }catch (Exception e){
            return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
}
