package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.delarosa.app.modules.general.entities.MetodoPago;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class PagoTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPagoTicket;

    @OneToOne
    @JoinColumn(name = "idTicket", referencedColumnName = "idTicket")
    private Ticket ticket;

    @OneToOne
    @JoinColumn(name = "idMetodoPago", referencedColumnName = "idMetodoPago")
    private MetodoPago metodoPago;

    @Column(nullable = false)
    private LocalDateTime fechaPago;

    @Column(nullable = false)
    private String referencia;
}
