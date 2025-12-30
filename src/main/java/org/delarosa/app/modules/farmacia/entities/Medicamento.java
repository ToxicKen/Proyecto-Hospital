package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMedicamento;

    @Column(unique = true)
    private String nombre;

    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    private Integer stock;

}
