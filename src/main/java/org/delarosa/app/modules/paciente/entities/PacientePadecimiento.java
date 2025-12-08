package org.delarosa.app.modules.paciente.entities;

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
    private PacientePadecimientoId idPadecimientoPaciente;

    @ManyToOne
    @MapsId("idPaciente")
    @JoinColumn(name = "idPaciente")
    private Paciente paciente;

    @ManyToOne
    @MapsId("idPadecimiento")
    @JoinColumn(name = "idPadecimiento")
    private Padecimiento padecimiento;

    @Column
    private String descripcion;

}
