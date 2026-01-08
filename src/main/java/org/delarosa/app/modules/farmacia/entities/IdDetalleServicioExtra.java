package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor   // 🔥 ESTE ES EL QUE FALTABA
public class IdDetalleServicioExtra implements Serializable {

    private Integer idTicket;
    private Integer idServicioExtra;
}