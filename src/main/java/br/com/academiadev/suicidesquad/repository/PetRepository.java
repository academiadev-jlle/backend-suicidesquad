package br.com.academiadev.suicidesquad.repository;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long>, QuerydslPredicateExecutor<Pet> {
    // Encontra os pets de um usuário cujo registro de situação mais recente precede a data de corte,
    // e que não foi notificado desde então (data do registro posterior à última notificação, ou nunca foi notificado)
    @Query("SELECT p\n" +
            "FROM Pet p\n" +
            "JOIN Registro r1\n" +
            "  ON r1.pet = p\n" +
            "LEFT OUTER JOIN Registro r2\n" +
            "  ON r1.pet = r2.pet\n" +
            "  AND r1.data < r2.data\n" +
            "WHERE r2.pet IS NULL\n" +
            "  AND p.usuario = ?1\n" +
            "  AND r1.data < ?2\n" +
            "  AND (p.dataNotificacaoDeInatividade < r1.data\n" +
            "       OR p.dataNotificacaoDeInatividade IS NULL)")
    List<Pet> findPetsDoUsuarioInativosNaoNotificadosDesde(Usuario usuario, LocalDateTime dataDeCorte);
}
