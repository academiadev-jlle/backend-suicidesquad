package br.com.academiadev.suicidesquad.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({AuditingEntityListener.class})
public class AuditableEntity<PK> extends BaseEntity<PK> {
    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(hidden = true)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @ApiModelProperty(hidden = true)
    private LocalDateTime lastModifiedDate;
}
