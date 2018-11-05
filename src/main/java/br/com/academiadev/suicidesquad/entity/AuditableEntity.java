package br.com.academiadev.suicidesquad.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
public class AuditableEntity<PK> extends BaseEntity<PK> {
    @CreatedDate
    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonProperty("last_modified_date")
    private LocalDateTime lastModifiedDate;
}
