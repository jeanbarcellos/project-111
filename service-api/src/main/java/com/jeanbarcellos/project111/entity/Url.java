package com.jeanbarcellos.project111.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(schema = "project111", name = "url")
public class Url {

    public static final Integer TTL_DAYS = 30;

    @Id
    @Column(name = "hash", length = 6, nullable = false)
    private String hash;

    @Column(name = "target_url", nullable = false)
    private String targetUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDate expiresAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "url_user_id_fk"))
    private User user;

    public Url(User user, String hash, String targetUrl) {
        this.hash = hash;
        this.targetUrl = targetUrl;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.toLocalDate().plusDays(TTL_DAYS);
    }
}