package br.com.academiadev.suicidesquad.mapper;

import br.com.academiadev.suicidesquad.dto.LocalizacaoDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioCreateDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioDTO;
import br.com.academiadev.suicidesquad.dto.UsuarioEditDTO;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.service.LocalizacaoService;
import br.com.academiadev.suicidesquad.service.PasswordService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {LocalizacaoMapper.class, TelefoneMapper.class}
)
public abstract class UsuarioMapper {

    @Autowired
    private LocalizacaoService localizacaoService;

    @Mappings({
            @Mapping(target = "nome"),
            @Mapping(target = "email"),
            @Mapping(target = "senha", ignore = true),
            @Mapping(target = "sexo", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "dataNascimento", source = "data_nascimento", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "localizacao", ignore = true)
    })
    public abstract Usuario toEntity(UsuarioCreateDTO dto);

    @Mappings({
            @Mapping(target = "nome"),
            @Mapping(target = "sexo", defaultValue = "NAO_INFORMADO"),
            @Mapping(target = "dataNascimento", source = "data_nascimento", dateFormat = "yyyy-MM-dd"),
            @Mapping(target = "localizacao", ignore = true),
            @Mapping(target = "telefones")
    })
    public abstract Usuario updateEntity(UsuarioEditDTO dto, @MappingTarget Usuario entity);

    @Mappings({
            @Mapping(target = "id"),
            @Mapping(target = "nome"),
            @Mapping(target = "data_criacao", source = "createdDate"),
            @Mapping(target = "email", source = "emailPublico")
    })
    public abstract UsuarioDTO toDto(Usuario entity);

    public abstract List<UsuarioDTO> toDtos(List<Usuario> entities);

    @AfterMapping
    public void mapearSenha(UsuarioCreateDTO usuarioCreateDTO, @MappingTarget Usuario entity) {
        entity.setSenha(PasswordService.encoder().encode(usuarioCreateDTO.getSenha()));
    }

    @AfterMapping
    public void mapearRelacoes(UsuarioEditDTO dto, @MappingTarget Usuario entity) {
        entity.getTelefones().forEach(telefone -> {
            telefone.setUsuario(entity);
        });
    }

    @AfterMapping
    public void mapearLocalizacao(UsuarioCreateDTO dto, @MappingTarget Usuario entity) {
        final LocalizacaoDTO localizacaoDTO = dto.getLocalizacao();
        if (localizacaoDTO != null) {
            entity.setLocalizacao(localizacaoService.findOrCreate(
                    localizacaoDTO.getBairro(),
                    localizacaoDTO.getCidade(),
                    localizacaoDTO.getUf()
            ));
        } else {
            entity.setLocalizacao(null);
        }
    }

    @AfterMapping
    public void mapearLocalizacao(UsuarioEditDTO dto, @MappingTarget Usuario entity) {
        final LocalizacaoDTO localizacaoDTO = dto.getLocalizacao();
        if (localizacaoDTO != null) {
            entity.setLocalizacao(localizacaoService.findOrCreate(
                    localizacaoDTO.getBairro(),
                    localizacaoDTO.getCidade(),
                    localizacaoDTO.getUf()
            ));
        } else {
            entity.setLocalizacao(null);
        }
    }
}
