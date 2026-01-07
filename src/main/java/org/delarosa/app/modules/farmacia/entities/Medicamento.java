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

    @Column(length = 100)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private Integer stock;

    @PrePersist
    public void prePersist() {
        if (this.activo == null) this.activo = true;
        if (this.stock == null) this.stock = 0;
    }

}
