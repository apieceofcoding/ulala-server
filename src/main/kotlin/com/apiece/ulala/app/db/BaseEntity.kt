package com.apiece.ulala.app.db

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(

    @CreatedDate
    @Column(updatable = false, nullable = false)
    var createdAt: LocalDateTime? = null,

    @CreatedBy
    @Column(updatable = false, nullable = false)
    var createdBy: String? = null,

    @LastModifiedDate
    @Column(nullable = false)
    var modifiedAt: LocalDateTime? = null,

    @LastModifiedBy
    @Column(nullable = false)
    var modifiedBy: String? = null,
)
