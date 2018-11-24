package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.LocalizacaoDTO;
import br.com.academiadev.suicidesquad.entity.Localizacao;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface LocalizacaoMapper {
    @Mappings({
            @Mapping(target = "bairro"),
            @Mapping(target = "cidade"),
            @Mapping(target = "uf")
    })
    Localizacao toEntity(LocalizacaoDTO dto);

    List<Localizacao> toEntities(List<LocalizacaoDTO> dtos);

    @InheritInverseConfiguration
    LocalizacaoDTO toDto(Localizacao entity);

    List<LocalizacaoDTO> toDtos(List<Localizacao> entities);

}
