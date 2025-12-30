package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicioExtra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idServicioExtra;

    @Column(unique = true)
    private String nombre;

    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

}
