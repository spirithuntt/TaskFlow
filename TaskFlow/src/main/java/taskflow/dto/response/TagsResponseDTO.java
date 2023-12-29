package taskflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagsResponseDTO {
    private Long id;
    private String name;
    private String status; //("success" or "error")
    private String message;

    public TagsResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
