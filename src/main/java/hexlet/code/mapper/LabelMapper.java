package hexlet.code.mapper;

import hexlet.code.dto.label.LabelCreateDto;
import hexlet.code.dto.label.LabelResponseDto;
import hexlet.code.dto.label.LabelUpdateDto;
import hexlet.code.model.Label;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {JsonNullableMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LabelMapper {

    LabelResponseDto toResponseDto(Label label);

    Label toEntity(LabelCreateDto dto);

    void update(LabelUpdateDto updateDto, @MappingTarget Label label);
}
