package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data

public class DetalleServicioExtra {
    @EmbeddedId
    private IdDetalleServicioExtra idDetalleServicioExtra;

    @ManyToOne
    @MapsId("idServicioExtra")
    @JoinColumn(name = "idServicioExtra")
    private ServicioExtra servicioExtra;

    @ManyToOne
    @MapsId("idTicket")
    @JoinColumn(name = "idTicket")
    private Ticket ticket;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private BigDecimal subtotal;


}
