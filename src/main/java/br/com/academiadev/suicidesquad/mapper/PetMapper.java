package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.PetCreateDTO;
import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.dto.PetDetailDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.enums.Raca;
import br.com.academiadev.suicidesquad.enums.Tipo;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LocalizacaoMapper.class, RegistroMapper.class}
)
public abstract class PetMapper {
    @Mappings({
            @Mapping(target = "tipo"),
            @Mapping(target = "porte"),
            @Mapping(target = "raca", ignore = true),
            @Mapping(target = "comprimentoPelo", source = "comprimento_pelo"),
            @Mapping(target = "sexo", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "categoria"),
            @Mapping(target = "vacinacao", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "castracao", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "nome"),
            @Mapping(target = "cores"),
            @Mapping(target = "localizacao"),
            @Mapping(target = "descricao")
    })
    public abstract Pet toEntity(PetCreateDTO dto);

    public abstract List<Pet> toEntities(List<PetCreateDTO> dtos);

    @Mappings({
            @Mapping(target = "data_criacao", source = "createdDate"),
            @Mapping(target = "data_edicao", source = "lastModifiedDate"),
            @Mapping(target = "tipo"),
            @Mapping(target = "porte"),
            @Mapping(target = "raca"),
            @Mapping(target = "comprimento_pelo", source = "comprimentoPelo"),
            @Mapping(target = "sexo"),
            @Mapping(target = "categoria"),
            @Mapping(target = "vacinacao"),
            @Mapping(target = "castracao"),
            @Mapping(target = "situacao_atual", source = "situacaoAtual"),
            @Mapping(target = "nome"),
            @Mapping(target = "cores"),
            @Mapping(target = "usuario_id", source = "usuario.id"),
            @Mapping(target = "usuario_nome", source = "usuario.nome"),
            @Mapping(target = "localizacao")
    })
    @Named("toDto")
    public abstract PetDTO toDto(Pet entity);

    @IterableMapping(qualifiedByName = "toDto")
    public abstract Iterable<PetDTO> toDtos(Iterable<Pet> entities);


    @Mappings({
            @Mapping(target = "data_criacao", source = "createdDate"),
            @Mapping(target = "data_edicao", source = "lastModifiedDate"),
            @Mapping(target = "tipo"),
            @Mapping(target = "porte"),
            @Mapping(target = "raca"),
            @Mapping(target = "comprimento_pelo", source = "comprimentoPelo"),
            @Mapping(target = "sexo"),
            @Mapping(target = "categoria"),
            @Mapping(target = "vacinacao"),
            @Mapping(target = "castracao"),
            @Mapping(target = "situacao_atual", source = "situacaoAtual"),
            @Mapping(target = "nome"),
            @Mapping(target = "descricao"),
            @Mapping(target = "cores"),
            @Mapping(target = "usuario_id", source = "usuario.id"),
            @Mapping(target = "usuario_nome", source = "usuario.nome"),
            @Mapping(target = "localizacao"),
            @Mapping(target = "usuario"),
            @Mapping(target = "registros"),
            @Mapping(target = "email", source = "usuario.email"),
//            @Mapping(target = "telefones", source = "usuario.telefones"),
    })
    public abstract PetDetailDTO toDetailDto(Pet entity);

    @AfterMapping
    public void mapearRaca(PetCreateDTO dto, @MappingTarget Pet entity) {
        if(dto.getRaca() != null) {
            entity.setRaca(Raca.valueOf(dto.getRaca()));
            return;
        }

        if (entity.getTipo() == Tipo.CACHORRO) {
            entity.setRaca(Raca.CACHORRO_SRD);
        } else if (entity.getTipo() == Tipo.GATO) {
            entity.setRaca(Raca.GATO_SRD);
        } else if (entity.getTipo() == Tipo.EQUINO) {
            entity.setRaca(Raca.EQUINO_SRD);
        } else {
            throw new RuntimeException("Tipo de pet inválido");
        }
    }
}
