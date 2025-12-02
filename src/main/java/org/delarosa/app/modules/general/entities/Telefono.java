package org.delarosa.app.modules.general.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
public class Telefono {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTelefono;
    @Column(nullable = false, unique = true)
    private String numeroTelefono;

}
