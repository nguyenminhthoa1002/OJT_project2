package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.LocationService;
import project.model.dto.request.LocationRequest;
import project.model.dto.response.LocationResponse;
import project.model.entity.Location;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class LocationImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Location>locationPage= locationRepository.findAll(pageable);
        Map<String,Object>result = Utility.returnResponse(locationPage);
        return result;
    }

    @Override
    public LocationResponse saveOrUpdate(LocationRequest locationRequest) {
        Location location=mapRequestToPoJo(locationRequest);
        Location locationNew = locationRepository.save(location);
        LocationResponse locationResponse=mapPoJoToResponse(locationNew);

        return locationResponse;
    }

    @Override
    public LocationResponse update(Integer id, LocationRequest locationRequest) {
        Location location = mapRequestToPoJo(locationRequest);
        location.setId(id);
        Location locationUpdate =locationRepository.save(location);
        LocationResponse locationResponse=mapPoJoToResponse(locationUpdate);
        return locationResponse;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
        Location location=locationRepository.findById(id).get();
        location.setStatus(0);
        locationRepository.save(location);
        return ResponseEntity.ok().body(Message.SUCCESS);
    }

    @Override
    public List<Location> findAll() {
        List<Location>locationList=locationRepository.findAll();
        return locationList;
    }

    @Override
    public List<LocationResponse> getAllForClient() {
        List<Location> locationList =locationRepository.findAll();
        List<LocationResponse> locationResponseList =new ArrayList<>();
        for (Location location:locationList) {
            LocationResponse locationResponse=new LocationResponse();
            locationResponse.setStatus(location.getStatus());
            locationResponse.setName(location.getName());
            locationResponseList.add(locationResponse);
        }
        return locationResponseList;
    }

    @Override
    public Location findById(Integer id) {
        Location location= locationRepository.findById(id).get();
        return location;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Location>locationPage=locationRepository.findByNameContaining(name,pageable);
        Map<String,Object>result=Utility.returnResponse(locationPage);
        return result;
    }

    @Override
    public Location mapRequestToPoJo(LocationRequest locationRequest) {
            Location location =new Location();
            location.setName(locationRequest.getName());
            location.setStatus(locationRequest.getStatus());

        return location;
    }

    @Override
    public LocationResponse mapPoJoToResponse(Location location) {
        LocationResponse locationResponse=new LocationResponse();
        locationResponse.setId(location.getId());
        locationResponse.setName(location.getName());
        locationResponse.setStatus(location.getStatus());
        return locationResponse;
    }
}
