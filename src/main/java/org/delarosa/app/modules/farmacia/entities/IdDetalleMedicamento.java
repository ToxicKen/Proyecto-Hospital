package org.delarosa.app.modules.farmacia.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Setter
@Getter
public class IdDetalleMedicamento {

    @Column(name = "idTicket", nullable = false)
    private Integer idTicket;

    @Column(name = "idMedicamento", nullable = false)
    private Integer idMedicamento;

}
