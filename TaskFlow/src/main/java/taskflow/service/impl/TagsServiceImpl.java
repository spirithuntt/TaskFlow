package taskflow.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import taskflow.dto.request.TagsRequestDTO;
import taskflow.dto.response.TagsResponseDTO;
import taskflow.entities.Tags;
import taskflow.repository.TagsRepository;
import taskflow.service.TagsService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagsServiceImpl implements TagsService {

    private final TagsRepository tagsRepository;
    private final ModelMapper modelMapper;

    public TagsServiceImpl(TagsRepository tagsRepository, ModelMapper modelMapper) {
        this.tagsRepository = tagsRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagsResponseDTO createTags(TagsRequestDTO tagsRequestDTO) {
        try {
            Tags tags = modelMapper.map(tagsRequestDTO, Tags.class);
            tags = tagsRepository.save(tags);

            // Check if the tag was added successfully
            if (tags.getId() != null) {
                return new TagsResponseDTO(tags.getId(), tags.getName(), "success", "Tag added successfully");
//                throw new RuntimeException("Error creating tags: " + tagsRequestDTO.getName()+ "This is a test exception");
            } else {
                return new TagsResponseDTO("error", "Failed to add tag");
            }
        } catch (Exception e) {
            return new TagsResponseDTO("error", "Error creating tags: " + e.getMessage());
        }
    }

    @Override
    public TagsResponseDTO getTags(Long id) {
        try {
            Tags tags = tagsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tags not found with id: " + id));
            return new TagsResponseDTO(tags.getId(), tags.getName(), "success", "Tag retrieved successfully");
        } catch (EntityNotFoundException e) {
            return new TagsResponseDTO("error", "Tags not found with id: " + id);
        } catch (Exception e) {
            return new TagsResponseDTO("error", "Error getting tags: " + e.getMessage());
        }
    }

    @Override
    public List<TagsResponseDTO> getAllTags() {
        try {
            List<Tags> tagsList = tagsRepository.findAll();
            return tagsList.stream()
                    .map(tags -> new TagsResponseDTO(tags.getId(), tags.getName(), "success", "Tags retrieved successfully"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.singletonList(new TagsResponseDTO("error", "Error getting tags: " + e.getMessage()));
        }
    }

    @Override
    public TagsResponseDTO updateTags(Long id, TagsRequestDTO tagsRequestDTO) {
        try {
            Tags existingTags = tagsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tags not found with id: " + id));
            modelMapper.map(tagsRequestDTO, existingTags);
            existingTags = tagsRepository.save(existingTags);
            return new TagsResponseDTO(existingTags.getId(), existingTags.getName(), "success", "Tag updated successfully");
        } catch (EntityNotFoundException e) {
            return new TagsResponseDTO("error", "Tags not found with id: " + id);
        } catch (Exception e) {
            return new TagsResponseDTO("error", "Error updating tags: " + e.getMessage());
        }
    }

    @Override
    public void deleteTags(Long id) {
        try {
            Tags tags = tagsRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Tags not found with id: " + id));
            tagsRepository.delete(tags);
            new TagsResponseDTO("success", "Tag deleted successfully");
        } catch (EntityNotFoundException e) {
            new TagsResponseDTO( "error", "Tags not found with id: " + id);
        } catch (Exception e) {
            new TagsResponseDTO("error", "Error deleting tags: " + e.getMessage());
        }
    }
}
