package br.com.academiadev.suicidesquad.service;

import br.com.academiadev.suicidesquad.entity.*;
import br.com.academiadev.suicidesquad.enums.Situacao;
import br.com.academiadev.suicidesquad.repository.PetRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    private final PetRepository petRepository;

    private final EntityManager entityManager;

    @Autowired
    public PetService(PetRepository petRepository, EntityManager entityManager) {
        this.petRepository = petRepository;
        this.entityManager = entityManager;
    }

    public List<Pet> findAll() {
        return petRepository.findAll();
    }

    public List<Pet> findPetsInativosNaoNotificadosDoUsuario(Usuario usuario) {
        // Pet inativo: não houve atualização da situação por 7 dias.
        LocalDateTime dataDeCorte = LocalDateTime.now().minusDays(7);
        return petRepository.findPetsDoUsuarioInativosNaoNotificadosDesde(usuario, dataDeCorte);
    }

    public Optional<Pet> findById(Long idPet) {
        return petRepository.findById(idPet);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public Iterable<Pet> search(PetSearch search) {
        final JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        final QPet pet = QPet.pet;
        final QRegistro registro = QRegistro.registro;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(pet.tipo.eq(search.getTipo()));

        if (search.getSexo() != null) {
            builder.and(pet.sexo.eq(search.getSexo()));
        }

        if (!search.getMostrarEntregues()) {
            final JPAQuery<Long> registroEntregueMaisRecente = queryFactory
                    .select(registro.id)
                    .from(registro)
                    .innerJoin(registro.pet, pet)
                    .having(registro.pet.eq(pet)
                            .and(registro.situacao.eq(Situacao.ENTREGUE))
                            .and(registro.data.eq(registro.data.max())))
                    .groupBy(registro.id);
            builder.andNot(pet.registros.any().id.in(registroEntregueMaisRecente));
        }

        if (!search.getPortes().isEmpty()) {
            builder.and(pet.porte.in(search.getPortes()));
        }

        if (!search.getRacas().isEmpty()) {
            builder.and(pet.raca.in(search.getRacas()));
        }

        if (!search.getPelos().isEmpty()) {
            builder.and(pet.comprimentoPelo.in(search.getPelos()));
        }

        if (!search.getCategorias().isEmpty()) {
            builder.and(pet.categoria.in(search.getCategorias()));
        }

        if (!search.getVacinacoes().isEmpty()) {
            builder.and(pet.vacinacao.in(search.getVacinacoes()));
        }

        if (!search.getCastracoes().isEmpty()) {
            builder.and(pet.castracao.in(search.getCastracoes()));
        }

        if (!search.getCores().isEmpty()) {
            search.getCores().forEach(cor -> {
                builder.and(pet.cores.contains(cor));
            });
        }

        return petRepository.findAll(builder);
    }

    public boolean existsById(Long idPet) {
        return petRepository.existsById(idPet);
    }

    public void deleteById(Long idPet) {
        petRepository.deleteById(idPet);
    }

    public boolean isPublicador(Authentication authentication, Long idPet) {
        Pet pet = petRepository.findById(idPet).orElse(null);
        final Usuario usuario = (Usuario) authentication.getPrincipal();
        return (pet != null) && (usuario != null) && (usuario.getId() != null) && pet.getUsuario().getId().equals(usuario.getId());
    }

    public void adicionarVisita(Pet pet, Usuario usuario) {
        pet.addVisita(usuario);
    }
}
