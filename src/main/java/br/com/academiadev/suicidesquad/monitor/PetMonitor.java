package br.com.academiadev.suicidesquad.monitor;

import br.com.academiadev.suicidesquad.entity.Pet;
import br.com.academiadev.suicidesquad.service.EmailService;
import br.com.academiadev.suicidesquad.service.PetService;
import br.com.academiadev.suicidesquad.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class PetMonitor {
    private final UsuarioService usuarioService;

    private final PetService petService;

    private final EmailService emailService;

    @Autowired
    public PetMonitor(PetService petService, EmailService emailService, UsuarioService usuarioService) {
        this.petService = petService;
        this.emailService = emailService;
        this.usuarioService = usuarioService;
    }

    private String buildCorpoEmailInatividade(List<Pet> pets) {
        StringBuilder builder = new StringBuilder();
        builder.append("<p>Olá, ").append(pets.get(0).getUsuario().getNome()).append(".</p>");

        if (pets.size() > 1) {
            builder
                    .append("<p>Faz algum tempo que seus pets não recebem atualizações. ")
                    .append("Caso haja alguma novidade, clique nos pets para acessá-los.</p>");
        } else {
            builder
                    .append("<p>Faz algum tempo que o seu pet não recebe atualização</p>")
                    .append("<p>Se houver alguma novidade, acesse a página do pet.</p>");
        }

        builder.append("<ul>");
        pets.stream().map(this::buildItemPet).forEach(builder::append);
        builder.append("</ul>");
        return builder.toString();
    }

    private String buildItemPet(Pet pet) {
        // TODO linkar o pet
        StringBuilder builder = new StringBuilder();
        builder.append("<li>");
        if (pet.getNome() != null) {
            builder.append(pet.getNome());
        } else if (pet.getRaca() != null) {
            builder.append(pet.getRaca()).append(" ").append(pet.getCategoria());
        } else {
            builder.append(pet.getTipo()).append(" ").append(pet.getCategoria());
        }
        builder.append("</li>");
        return builder.toString();
    }

    private void enviarEmailInatividade(List<Pet> pets) {
        if (pets.size() == 0) {
            return;
        }

        pets.forEach(pet -> {
            pet.setDataNotificacaoDeInatividade(LocalDateTime.now());
            petService.save(pet);
        });

        emailService.enviarParaUsuario(
                pets.get(0).getUsuario(),
                pets.size() > 1 ? "Alguma notícia dos seus pets?" : "Alguma notícia do seu pet?",
                buildCorpoEmailInatividade(pets)
        );
    }

    // A cada 1 dia, enviar emails de notificação de inatividade
    @Scheduled(fixedRate = 86400000)
    public void notificarInatividade() {
        usuarioService.findAll().stream()
                .map(petService::findPetsInativosNaoNotificadosDoUsuario)
                .forEach(this::enviarEmailInatividade);
    }
}
