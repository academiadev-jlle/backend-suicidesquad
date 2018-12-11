package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.dto.PetDTO;
import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.mapper.PetMapper;
import br.com.academiadev.suicidesquad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PetMapper petMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PetMapper petMapper) {
        this.usuarioRepository = usuarioRepository;
        this.petMapper = petMapper;
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }


    public Optional<Usuario> findByFacebookUserId(String facebookUserId) {
        if (facebookUserId == null) {
            return Optional.empty();
        }
        return usuarioRepository.findByFacebookUserId(facebookUserId);
    }

    public boolean existsById(Long idUsuario) {
        return usuarioRepository.existsById(idUsuario);
    }

    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public boolean existsByFacebookUserId(String facebookUserId) {
        return usuarioRepository.existsByFacebookUserId(facebookUserId);
    }

    public void deleteById(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }

    public void adicionarFavorito(Usuario usuario, Pet pet){
        usuario.addPetFavorito(pet);
    }

    public Iterable<PetDTO> findPetsByUsuarioId(Usuario usuario){
        return petMapper.toDtos(usuario.getPets());
    }
}
