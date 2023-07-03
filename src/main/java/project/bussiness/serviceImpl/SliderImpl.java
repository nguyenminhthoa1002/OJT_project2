package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.SliderService;
import project.model.dto.request.SliderRequest;
import project.model.dto.response.SliderResponse;
import project.model.entity.Slider;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.SliderRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SliderImpl implements SliderService {
    @Autowired
    private SliderRepository sliderRepository;

    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Slider> page = sliderRepository.findAll(pageable);
        Map<String,Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public SliderResponse saveOrUpdate(SliderRequest sliderRequest) {
        Slider slider = mapRequestToPoJo(sliderRequest);
        Slider slider1 = sliderRepository.save(slider);
        SliderResponse sliderResponse = mapPoJoToResponse(slider1);
        return sliderResponse;
    }

    @Override
    public SliderResponse update(Integer id, SliderRequest sliderRequest) {
        Slider sliderUpdate = findById(id);
        sliderUpdate.setName(sliderRequest.getName());
        sliderUpdate.setSliderBackground(sliderRequest.getSliderBackground());
        sliderUpdate.setSliderDescription(sliderRequest.getSliderDescription());
        sliderUpdate.setStatus(sliderRequest.getStatus());
        Slider slider = sliderRepository.save(sliderUpdate);
        return mapPoJoToResponse(slider);
    }
    @Override
    public ResponseEntity<?> delete(Integer id) {
        try {
            Slider sliderDelete = findById(id);
            sliderDelete.setStatus(0);
            sliderRepository.save(sliderDelete);
           return ResponseEntity.ok().body(Message.SUCCESS);
        } catch (Exception ex) {
          return ResponseEntity.badRequest().body(Message.ERROR_400);
        }
    }
    @Override
    public List<Slider> findAll() {
        List<Slider> responses= sliderRepository.findAll();
        return responses;
    }

    @Override
    public List<SliderResponse> getAllForClient() {
        List<SliderResponse> responses = findAll().stream()
                .map(this::mapPoJoToResponse)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public Slider findById(Integer id) {
        return sliderRepository.findById(id).get();
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Slider> page = sliderRepository.findByNameContaining(name, pageable);
        Map<String, Object> result = Utility.returnResponse(page);
        return result;
    }

    @Override
    public Slider mapRequestToPoJo(SliderRequest sliderRequest) {
        Slider slider = new Slider();
        slider.setName(sliderRequest.getName());
        slider.setSliderBackground(sliderRequest.getSliderBackground());
        slider.setSliderDescription(sliderRequest.getSliderDescription());
        slider.setStatus(1);
        return slider;
    }

    @Override
    public SliderResponse mapPoJoToResponse(Slider slider) {
        SliderResponse sliderResponse = new SliderResponse();
        sliderResponse.setId(slider.getId());
        sliderResponse.setName(slider.getName());
        sliderResponse.setSliderBackground(slider.getSliderBackground());
        sliderResponse.setSliderDescription(slider.getSliderDescription());
        return sliderResponse;
    }
}
