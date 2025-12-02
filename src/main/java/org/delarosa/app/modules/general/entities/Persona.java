package org.delarosa.app.modules.general.entities;

import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.persona.PersonaTelefono;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPersona;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidoP;

    private String apellidoM;

    @Column(unique = true, nullable = false)
    private String curp;

    @Column(nullable = false)
    private String colonia;

    @Column(nullable = false)
    private String calle;

    @Column(nullable = false)
    private String numero;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonaTelefono> telefonos = new ArrayList<>();

    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Usuario usuario;

    public void addTelefono(PersonaTelefono personaTelefono) {
        this.telefonos.add(personaTelefono);
        personaTelefono.setPersona(this);
    }

}
