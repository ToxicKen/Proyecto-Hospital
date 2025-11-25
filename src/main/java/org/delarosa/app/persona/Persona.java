package org.delarosa.app.persona;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;
@Entity
@Data
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

    private String  numero;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<PersonaTelefono> telefonos = new ArrayList<>();


    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private Usuario usuario;
    
    public void addTelefono(PersonaTelefono personaTelefono) {
        this.telefonos.add(personaTelefono);
        personaTelefono.setPersona(this);
    }


}
