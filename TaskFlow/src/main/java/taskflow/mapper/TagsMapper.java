package taskflow.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import taskflow.dto.request.TagsRequestDTO;
import taskflow.dto.response.TagsResponseDTO;
import taskflow.entities.Tags;

@Mapper
public interface TagsMapper {
    TagsMapper INSTANCE = Mappers.getMapper(TagsMapper.class);

    @Mapping(target = "id", ignore = true)
    Tags tagsRequestDTOToEntity(TagsRequestDTO requestDTO);

    TagsResponseDTO tagsToTagsResponseDTO(Tags tags);
}
