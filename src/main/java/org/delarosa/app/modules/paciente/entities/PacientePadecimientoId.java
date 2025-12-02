package org.delarosa.app.modules.paciente.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class PacientePadecimientoId {

    @Column(nullable = false)
    private Integer idPadecimiento;
    
    @Column(nullable = false)
    private Integer idPaciente;
}
