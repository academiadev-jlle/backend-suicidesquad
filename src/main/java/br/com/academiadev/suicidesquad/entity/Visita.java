package br.com.academiadev.suicidesquad.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
public class Visita extends BaseEntity<Long> {

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dataVisita;

    public Visita(Pet pet, Usuario usuario) {
        this.pet = pet;
        this.usuario = usuario;
    }
}


