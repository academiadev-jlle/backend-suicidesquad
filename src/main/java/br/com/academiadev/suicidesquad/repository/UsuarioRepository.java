package br.com.academiadev.suicidesquad.repository;

import br.com.academiadev.suicidesquad.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {


}
