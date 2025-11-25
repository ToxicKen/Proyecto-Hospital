package org.delarosa.app.usuario;


import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.persona.Persona;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
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




}
