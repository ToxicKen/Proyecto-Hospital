package org.delarosa.app.paciente;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class PacientePadecimientoId {
    private Integer idPadecimiento;
    private Integer idPaciente;
}
