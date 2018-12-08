package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.VisitaDTO;
import br.com.academiadev.suicidesquad.entity.Visita;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {PetMapper.class}
)
public interface VisitaMapper {
    @Mappings({
            @Mapping(target = "usuario_id", source = "usuario.id"),
            @Mapping(target = "pet"),
            @Mapping(target = "data")
    })
    VisitaDTO toDto(Visita entity);

    List<VisitaDTO> toDtos(List<Visita> entities);
}
