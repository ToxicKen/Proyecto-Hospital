package org.delarosa.app.modules.security.entities;


import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.persona.Persona;
import org.delarosa.app.usuario.Rol;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    @Column(unique = true, nullable = false)
    private String correoElectronico;

    @Column(nullable = false)
    private String contrasenia;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idPersona", referencedColumnName = "idPersona")
    private Persona persona;

    @ManyToMany
    @JoinTable(
            name = "UsuarioRol"
            , joinColumns = @JoinColumn(name = "idUsuario")
            , inverseJoinColumns = @JoinColumn(name = "idRol"))
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public void addRol(Rol rol) {
        roles.add(rol);
        rol.getUsuarios().add(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        return roles.stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre().name()))
                .collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return contrasenia;
    }

    @Override
    public String getUsername() {
        return correoElectronico;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
