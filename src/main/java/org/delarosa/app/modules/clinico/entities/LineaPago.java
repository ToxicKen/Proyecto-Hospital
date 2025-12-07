package org.delarosa.app.modules.clinico.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LineaPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLineaPago;

    @ManyToOne
    @JoinColumn(name ="idOrdenPago",referencedColumnName = "idOrdenPago")
    private OrdenPago ordenPago;

    private LocalDateTime fechaPago;

    private BigDecimal montoPago;

}


