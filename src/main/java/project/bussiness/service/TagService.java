package project.bussiness.service;

import project.model.dto.request.TagRequest;
import project.model.dto.response.TagResponse;
import project.model.entity.Tags;

public interface TagService extends RootService<Tags,Integer, TagRequest, TagResponse>{
}
