package org.delarosa.app.modules.clinico.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrdenPago;

    @OneToOne
    @JoinColumn(name ="folioCita",referencedColumnName = "folioCita")
    private Cita cita;

    private BigDecimal montoTotal;

    private Boolean estatus;

}
