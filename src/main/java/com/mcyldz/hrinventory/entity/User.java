package com.mcyldz.hrinventory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = true, last_modified_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("is_deleted = false")
public class User extends BaseEntity{

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personnel_id", referencedColumnName = "id", unique = true)
    private Personnel personnel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false, referencedColumnName = "id")
    private Role role;
}
