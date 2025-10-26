package hexlet.code.mapper;

import hexlet.code.dto.task_status.TaskStatusCreateDto;
import hexlet.code.dto.task_status.TaskStatusResponseDto;
import hexlet.code.dto.task_status.TaskStatusUpdateDto;
import hexlet.code.model.TaskStatus;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {JsonNullableMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskStatusMapper {

    TaskStatusResponseDto toResponseDto(TaskStatus taskStatus);

    TaskStatus toEntity(TaskStatusCreateDto dto);

    void update(TaskStatusUpdateDto updateDto, @MappingTarget TaskStatus taskStatus);
}
