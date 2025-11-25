package org.delarosa.app.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PacientePadecimiento {

    @EmbeddedId
    private Integer idPadecimientoPaciente;

    @ManyToOne
    @MapsId("pacienteId")
    private Paciente paciente;

    @ManyToOne
    @MapsId("padecimientoId")
    private Padecimiento padecimiento;

    @Column
    private String descripcion;

}
