package org.delarosa.app.usuario;


import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.persona.Persona;
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
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String correoElectronico;

    private String contrasenia;

    private LocalDateTime fechaCreacion;

   @OneToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "idPersona",
           referencedColumnName = "idPersona")
    private Persona persona;

    @ManyToMany
    @JoinTable(
            name = "UsuarioRol"
            , joinColumns = @JoinColumn(name = "idUsuario")
            , inverseJoinColumns = @JoinColumn(name = "idRol"))
    private Set<Rol> roles = new HashSet<>();

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null || roles.isEmpty()) {
            return List.of();
        }
        // Convierte tus roles (ej: "ADMIN") a autoridades de Spring (ej: "ROLE_ADMIN")
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
        return  correoElectronico;
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
