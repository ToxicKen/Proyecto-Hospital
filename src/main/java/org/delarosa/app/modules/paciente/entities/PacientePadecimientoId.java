package org.delarosa.app.modules.paciente.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Setter
@Getter
public class PacientePadecimientoId {

    @Column(name = "idPaciente",nullable = false)
    private Integer idPaciente;

    @Column(name =  "idPadecimiento",nullable = false)
    private Integer idPadecimiento;

}
