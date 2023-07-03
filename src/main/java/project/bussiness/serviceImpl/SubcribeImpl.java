package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.SubcribeService;
import project.model.dto.request.SubcribeRequest;
import project.model.dto.response.SubcribeResponse;
import project.model.entity.SubcribeEmail;
import project.repository.SubcribeEmailRepository;

import java.util.List;
import java.util.Map;

@Service
public class SubcribeImpl implements SubcribeService {
    @Autowired
    private SubcribeEmailRepository subcribeEmailRepository;
    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        return null;
    }

    @Override
    public SubcribeResponse saveOrUpdate(SubcribeRequest subcribeRequest) {
        return null;
    }

    @Override
    public SubcribeResponse update(Integer id, SubcribeRequest subcribeRequest) {
        return null;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
      return null  ;
    }

    @Override
    public List<SubcribeEmail> findAll() {
        return null;
    }

    @Override
    public List<SubcribeResponse> getAllForClient() {
        return null;
    }

    @Override
    public SubcribeEmail findById(Integer id) {
        return null;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public SubcribeEmail mapRequestToPoJo(SubcribeRequest subcribeRequest) {
        return null;
    }

    @Override
    public SubcribeResponse mapPoJoToResponse(SubcribeEmail subcribeEmail) {
        SubcribeResponse sr = new SubcribeResponse();
        sr.setId(subcribeEmail.getId());
        sr.setSubcribeEmail(subcribeEmail.getSubcribeEmail());
        return sr;
    }


    @Override
    public SubcribeResponse getSubcribe(String email) {
        SubcribeEmail newSub = new SubcribeEmail();
        newSub.setSubcribeEmail(email);
        newSub.setStatus(1);
        SubcribeEmail subcribeEmail = subcribeEmailRepository.save(newSub);
        return mapPoJoToResponse(subcribeEmail);
    }
}
