package project.bussiness.service;

import project.model.dto.request.SubcribeRequest;
import project.model.dto.response.SubcribeResponse;
import project.model.entity.SubcribeEmail;

public interface SubcribeService extends RootService<SubcribeEmail,Integer, SubcribeRequest, SubcribeResponse> {
    SubcribeResponse getSubcribe(String email);
}
