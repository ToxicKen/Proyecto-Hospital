package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class IdDetalleServicioExtra {
    @Column(name = "idTicket", nullable = false)
    private Integer idTicket;

    @Column(name = "idServicioExtra", nullable = false)
    private Integer idServicioExtra;

}
