package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.Usuario;
import br.com.academiadev.suicidesquad.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
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

    public void deleteById(Long idUsuario) {
        usuarioRepository.deleteById(idUsuario);
    }
}
