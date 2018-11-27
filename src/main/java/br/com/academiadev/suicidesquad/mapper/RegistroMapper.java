package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.RegistroCreateDTO;
import br.com.academiadev.suicidesquad.dto.RegistroDTO;
import br.com.academiadev.suicidesquad.entity.Registro;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LocalDateTime.class}
)
public interface RegistroMapper {
    @Mappings({
            @Mapping(target = "situacao"),
            @Mapping(target = "data", dateFormat = "yyyy-MM-dd HH:MM:ss")
    })
    Registro toEntity(RegistroDTO dto);

    @Mappings({
            @Mapping(target = "situacao"),
            @Mapping(target = "data", expression = "java(LocalDateTime.now())")
    })
    Registro toEntity(RegistroCreateDTO dto);

    @InheritInverseConfiguration
    RegistroDTO toDto(Registro entity);

    List<RegistroDTO> toDtos(List<Registro> entities);

}
