package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.*;
import br.com.academiadev.suicidesquad.entity.PetFavorito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = {PetMapper.class}
)

public abstract class PetFavoritoMapper {

    @Mappings({
            @Mapping(target = "pet"),
            @Mapping(target = "data_criacao", source = "createdDate")
    })
    public abstract PetFavoritoDTO toDTO(PetFavorito petFavorito);

}
