package taskflow.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate deadline;
}
