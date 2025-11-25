package org.delarosa.app.persona;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonaTelefono {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPersonaTelefono;

    @ManyToOne
    @JoinColumn(name = "idPersona",referencedColumnName = "idPersona")
    private Persona persona;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idTelefono",referencedColumnName = "idPersona")
    private Telefono telefono;

    private String tipo;
}
