package taskflow.service;

import taskflow.dto.request.TagsRequestDTO;
import taskflow.dto.response.TagsResponseDTO;

import java.util.List;

public interface TagsService {
    TagsResponseDTO createTags(TagsRequestDTO tagsRequestDTO);
    TagsResponseDTO getTags(Long id);
    List<TagsResponseDTO> getAllTags();
    TagsResponseDTO updateTags(Long id, TagsRequestDTO tagsRequestDTO);
    void deleteTags(Long id);
}
