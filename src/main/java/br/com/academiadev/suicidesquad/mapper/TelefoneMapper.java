package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.TelefoneDTO;
import br.com.academiadev.suicidesquad.entity.Telefone;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TelefoneMapper {
    @Mappings({
            @Mapping(target = "numero"),
            @Mapping(target = "whatsapp", defaultValue = "false")
    })
    Telefone toEntity(TelefoneDTO dto);

    List<Telefone> toEntities(List<TelefoneDTO> dtos);

    @InheritInverseConfiguration
    TelefoneDTO toDto(Telefone entity);

    List<TelefoneDTO> toDtos(List<Telefone> dtos);
}
