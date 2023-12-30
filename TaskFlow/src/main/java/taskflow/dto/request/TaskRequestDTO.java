package taskflow.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate deadline;
    private List<Long> tagIds;
    private Long createdById;
}
