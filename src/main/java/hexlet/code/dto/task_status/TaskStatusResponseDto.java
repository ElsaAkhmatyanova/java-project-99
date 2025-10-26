package hexlet.code.dto.task_status;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TaskStatusResponseDto {
    private Long id;
    private String name;
    private String slug;
    private LocalDate createdAt;
}
