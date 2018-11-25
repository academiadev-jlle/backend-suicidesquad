package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.UsuarioCreateDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioEditDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.service.PasswordService;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LocalizacaoMapper.class, TelefoneMapper.class}
)
public abstract class UsuarioMapper {
    @Mappings({
            @Mapping(target = "nome"),
            @Mapping(target = "email"),
            @Mapping(target = "senha", ignore = true),
            @Mapping(target = "sexo", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "dataNascimento", source = "data_nascimento", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "localizacao")
    })
    public abstract Usuario toEntity(UsuarioCreateDTO dto);

    @Mappings({
            @Mapping(target = "nome"),
            @Mapping(target = "email"),
            @Mapping(target = "senha", ignore = true),
            @Mapping(target = "sexo", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "dataNascimento", source = "data_nascimento", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "localizacao"),
            @Mapping(target = "telefones")
    })
    public abstract Usuario toEntity(UsuarioEditDTO dto);

    @Mappings({
            @Mapping(target = "nome"),
            @Mapping(target = "data_criacao", source = "createdDate")
    })
    public abstract UsuarioDTO toDto(Usuario entity);

    public abstract List<UsuarioDTO> toDtos(List<Usuario> entities);

    @AfterMapping
    public void mapearSenha(UsuarioCreateDTO usuarioCreateDTO, @MappingTarget Usuario entity) {
        entity.setSenha(PasswordService.encoder().encode(usuarioCreateDTO.getSenha()));
    }

    @AfterMapping
    public void mapearSenha(UsuarioEditDTO usuarioEditDTO, @MappingTarget Usuario entity) {
        if (usuarioEditDTO.getSenha() != null) {
            entity.setSenha(PasswordService.encoder().encode(usuarioEditDTO.getSenha()));
        }
    }
}
