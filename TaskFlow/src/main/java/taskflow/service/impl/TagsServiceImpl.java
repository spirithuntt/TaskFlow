package taskflow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import taskflow.dto.request.TagsRequestDTO;
import taskflow.dto.response.TagsResponseDTO;
import taskflow.entities.Tags;
import taskflow.mapper.TagsMapper;
import taskflow.repository.TagsRepository;
import taskflow.service.TagsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagsServiceImpl implements TagsService {

    private final TagsRepository tagsRepository;
    private final TagsMapper tagsMapper;

    @Autowired
    public TagsServiceImpl(TagsRepository tagsRepository, TagsMapper tagsMapper) {
        this.tagsRepository = tagsRepository;
        this.tagsMapper = tagsMapper;
    }

    @Override
    public List<TagsResponseDTO> getAllTags() {
        List<Tags> tags = tagsRepository.findAll();
        return tags.stream()
                .map(tagsMapper::tagsToTagsResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagsResponseDTO getTagById(Long id) {
        Tags tag = tagsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        return tagsMapper.tagsToTagsResponseDTO(tag);
    }

    @Override
    public TagsResponseDTO createTag(TagsRequestDTO requestDTO) {
        Tags tagToSave = tagsMapper.tagsRequestDTOToEntity(requestDTO);
        Tags savedTag = tagsRepository.save(tagToSave);
        return tagsMapper.tagsToTagsResponseDTO(savedTag);
    }

    @Override
    public TagsResponseDTO updateTag(Long id, TagsRequestDTO requestDTO) {
        Tags existingTag = tagsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        // Update existingTag with data from requestDTO
        tagsMapper.tagsRequestDTOToEntity(requestDTO);
        Tags updatedTag = tagsRepository.save(existingTag);
        return tagsMapper.tagsToTagsResponseDTO(updatedTag);
    }

    @Override
    public void deleteTag(Long id) {
        tagsRepository.deleteById(id);
    }
}
