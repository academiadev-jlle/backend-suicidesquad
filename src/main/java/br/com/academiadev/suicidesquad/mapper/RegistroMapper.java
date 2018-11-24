package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.RegistroDTO;
import br.com.academiadev.suicidesquad.entity.Registro;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RegistroMapper {
    @Mappings({
            @Mapping(target = "situacao"),
            @Mapping(target = "data", dateFormat = "yyyy-MM-dd HH:MM:ss")
    })
    Registro toEntity(RegistroDTO dto);

    List<Registro> toEntities(List<RegistroDTO> dtos);

    @InheritInverseConfiguration
    RegistroDTO toDto(Registro entity);

    List<RegistroDTO> toDtos(List<Registro> entities);

}
