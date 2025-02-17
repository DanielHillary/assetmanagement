package com.venduit.assetmanagement.transaction_service.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createdDate", "lastModifiedDate", "deletedAt"},
        allowGetters = true
)
@Data
public abstract class Auditable<U> implements Serializable {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime lastModifiedDate;

    @LastModifiedDate
    @Column(nullable = true)
    protected LocalDateTime deletedAt;

    @CreatedBy
    protected U createdBy;

    @LastModifiedBy
    protected U lastModifiedBy;

}

