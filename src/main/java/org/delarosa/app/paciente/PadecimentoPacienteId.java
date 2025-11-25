package org.delarosa.app.paciente;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class PadecimentoPacienteId {
    private Integer idPersona;
    private Integer idPaciente;
}
