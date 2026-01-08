package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdDetalleMedicamento implements Serializable {

    @Column(name = "idTicket")
    private Integer idTicket;

    @Column(name = "idMedicamento")
    private Integer idMedicamento;
}

