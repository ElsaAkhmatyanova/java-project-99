package hexlet.code.dto.label;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LabelResponseDto {
    private Long id;
    private String name;
    private LocalDate createdAt;
}
