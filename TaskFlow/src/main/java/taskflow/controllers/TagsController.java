package taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.request.TagsRequestDTO;
import taskflow.dto.response.TagsResponseDTO;
import taskflow.service.TagsService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {

    private final TagsService tagsService;

    @Autowired
    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping
    public ResponseEntity<List<TagsResponseDTO>> getAllTags() {
        List<TagsResponseDTO> tags = tagsService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagsResponseDTO> getTagById(@PathVariable Long id) {
        TagsResponseDTO tag = tagsService.getTagById(id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<TagsResponseDTO> createTag(@RequestBody TagsRequestDTO requestDTO) {
        TagsResponseDTO createdTag = tagsService.createTag(requestDTO);
        return ResponseEntity.ok(createdTag);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagsResponseDTO> updateTag(@PathVariable Long id, @RequestBody TagsRequestDTO requestDTO) {
        TagsResponseDTO updatedTag = tagsService.updateTag(id, requestDTO);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagsService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
