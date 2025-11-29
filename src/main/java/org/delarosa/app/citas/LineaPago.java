package org.delarosa.app.citas;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
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


