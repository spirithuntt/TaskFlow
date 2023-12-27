package taskflow.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TagsRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
}
