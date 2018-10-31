package br.com.academiadev.suicidesquad.repository;

import br.com.academiadev.suicidesquad.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


}
