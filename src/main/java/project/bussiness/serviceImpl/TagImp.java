package project.bussiness.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.bussiness.service.TagService;
import project.model.dto.request.TagRequest;
import project.model.dto.response.TagResponse;
import project.model.entity.Tags;
import project.model.shopMess.Message;
import project.model.utility.Utility;
import project.repository.TagsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class TagImp implements TagService {
    @Autowired
    private TagsRepository tagsRepository;
    @Override
    public Map<String, Object> getPagingAndSort(Pageable pageable) {
        Page<Tags>tagsPage=tagsRepository.findAll(pageable);
        Map<String,Object>result =Utility.returnResponse(tagsPage);
        return result;
    }

    @Override
    public TagResponse saveOrUpdate(TagRequest tagRequest) {
            Tags tags=mapRequestToPoJo(tagRequest);
            Tags tagsNew = tagsRepository.save(tags);
            TagResponse tagResponse=mapPoJoToResponse(tagsNew);

        return tagResponse;
    }

    @Override
    public TagResponse update(Integer id, TagRequest tagRequest) {
        Tags tags=mapRequestToPoJo(tagRequest);
        tags.setId(id);
        Tags tagsUpdate = tagsRepository.save(tags);
        TagResponse tagResponse =mapPoJoToResponse(tagsUpdate);
        return tagResponse;
    }

    @Override
    public ResponseEntity<?> delete(Integer id) {
      try {
          Tags tagsDele=tagsRepository.findById(id).get();
          tagsDele.setStatus(0);
          tagsRepository.save(tagsDele);
          return ResponseEntity.ok().body(Message.SUCCESS);
      }catch (Exception e){
          return ResponseEntity.badRequest().body(Message.ERROR_400);
      }
    }

    @Override
    public List<Tags> findAll() {
        List<Tags> tagsList=tagsRepository.findAll();
        return tagsList;
    }

    @Override
    public List<TagResponse> getAllForClient() {
        List<Tags>list=tagsRepository.findAll();
        List<TagResponse>tagResponsesList=new ArrayList<>();
        for (Tags tags:list) {
            TagResponse tagResponse=new TagResponse();
            tagResponse.setId(tags.getId());
            tagResponse.setName(tags.getName());
            tagResponse.setStatus(tags.getStatus());
            tagResponsesList.add(tagResponse);

        }
        return tagResponsesList;
    }

    @Override
    public Tags findById(Integer id) {
        Tags tags=tagsRepository.findById(id).get();
        return tags;
    }

    @Override
    public Map<String, Object> findByName(String name, Pageable pageable) {
        Page<Tags>tagsPage=tagsRepository.findByNameContaining(name,pageable);
        Map<String,Object>result= Utility.returnResponse(tagsPage);
        return result;
    }

    @Override
    public Tags mapRequestToPoJo(TagRequest tagRequest) {
        Tags tags =new Tags();
        tags.setName(tagRequest.getName());
        tags.setStatus(tagRequest.getStatus());
        return tags;
    }

    @Override
    public TagResponse mapPoJoToResponse(Tags tags) {
        TagResponse tagResponse=new TagResponse();
        tagResponse.setId(tags.getId());
        tagResponse.setName(tags.getName());
        tagResponse.setStatus(tags.getStatus());
        return tagResponse;
    }
}
