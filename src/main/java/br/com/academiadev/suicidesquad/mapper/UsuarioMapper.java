package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.UsuarioCreateDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.service.PasswordService;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LocalizacaoMapper.class}
)
public abstract class UsuarioMapper {
    @Mappings({
            @Mapping(target = "nome"),
            @Mapping(target = "email"),
            @Mapping(target = "senha", ignore = true),
            @Mapping(target = "sexo", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "dataNascimento", source = "data_nascimento", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "localizacao"),
            @Mapping(target = "telefonePublico", source = "telefonePublico")
    })
    public abstract Usuario toEntity(UsuarioCreateDTO dto);

    public abstract List<Usuario> toEntities(List<UsuarioCreateDTO> dtos);

    @Mappings({
            @Mapping(target = "nome"),
            @Mapping(target = "data_criacao", source = "createdDate"),
            @Mapping(target = "telefonePublico", source = "telefonePublico")
    })
    public abstract UsuarioDTO toDto(Usuario entity);

    public abstract List<UsuarioDTO> toDtos(List<Usuario> entities);

    @AfterMapping
    public void mapearSenha(UsuarioCreateDTO usuarioCreateDTO, @MappingTarget Usuario entity) {
        entity.setSenha(PasswordService.encoder().encode(usuarioCreateDTO.getSenha()));
    }
}
