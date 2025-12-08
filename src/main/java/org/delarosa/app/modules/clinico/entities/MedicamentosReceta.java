package org.delarosa.app.modules.clinico.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicamentosReceta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMedicamentosReceta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folioReceta")
    @ToString.Exclude
    private Receta receta;

    private String nombre;

    private String tratamiento;

    private Integer cantidad;

}
