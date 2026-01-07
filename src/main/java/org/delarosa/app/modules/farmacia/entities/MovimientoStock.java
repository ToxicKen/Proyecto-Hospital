package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.modules.farmacia.enums.TipoMovimiento;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovimientoStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimientoStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false,name = "idMedicamento", referencedColumnName = "idMedicamento")
    private Medicamento medicamento;

    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockFinal;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    private LocalDateTime fecha;
    private String motivo;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }
}
