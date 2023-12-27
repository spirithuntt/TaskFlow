package taskflow.service;

import taskflow.dto.request.TagsRequestDTO;
import taskflow.dto.response.TagsResponseDTO;

import java.util.List;

public interface TagsService {

    List<TagsResponseDTO> getAllTags();

    TagsResponseDTO getTagById(Long id);

    TagsResponseDTO createTag(TagsRequestDTO requestDTO);

    TagsResponseDTO updateTag(Long id, TagsRequestDTO requestDTO);

    void deleteTag(Long id);
}
