package taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.request.TagsRequestDTO;
import taskflow.dto.response.TagsResponseDTO;
import taskflow.service.TagsService;

import java.util.List;

@RestController
@RequestMapping("api/v1/tags")
public class TagsController {

    private final TagsService tagsService;

    public TagsController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @PostMapping
    public ResponseEntity<TagsResponseDTO> createTags(@RequestBody TagsRequestDTO tagsRequestDTO) {
        TagsResponseDTO createdTags = tagsService.createTags(tagsRequestDTO);
        return new ResponseEntity<>(createdTags, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagsResponseDTO> getTags(@PathVariable Long id) {
        TagsResponseDTO tags = tagsService.getTags(id);
        return ResponseEntity.ok(tags);
    }

    @GetMapping
    public ResponseEntity<List<TagsResponseDTO>> getAllTags() {
        List<TagsResponseDTO> tagsList = tagsService.getAllTags();
        return ResponseEntity.ok(tagsList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagsResponseDTO> updateTags(@PathVariable Long id, @RequestBody TagsRequestDTO tagsRequestDTO) {
        TagsResponseDTO updatedTags = tagsService.updateTags(id, tagsRequestDTO);
        return ResponseEntity.ok(updatedTags);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTags(@PathVariable Long id) {
        tagsService.deleteTags(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

