package me.golf.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity {

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP",)
    var createdDate: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "modified_at", nullable = false, columnDefinition = "datetime DEFAULT CURRENT_TIMESTAMP")
    var modifiedDate: LocalDateTime? = null
}