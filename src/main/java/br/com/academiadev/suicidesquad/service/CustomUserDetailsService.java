package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Username na verdade é o email
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username \"%s\" not found", username)));
    }
}
