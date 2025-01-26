package com.jeanbarcellos.project111.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
@Entity
@DynamicUpdate
@Table(schema = "project111", name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "user_email_uk", columnNames = { "email" })
})
public class User {

    @Id
    @Column(name = "id", updatable = false)
    @Setter(value = AccessLevel.PRIVATE)
    @EqualsAndHashCode.Include
    protected UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

}
