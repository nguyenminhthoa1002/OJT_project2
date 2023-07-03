package project.bussiness.service;

import project.model.dto.request.LocationRequest;
import project.model.dto.response.LocationResponse;
import project.model.entity.Location;

public interface LocationService extends RootService<Location,Integer, LocationRequest, LocationResponse>{
}
