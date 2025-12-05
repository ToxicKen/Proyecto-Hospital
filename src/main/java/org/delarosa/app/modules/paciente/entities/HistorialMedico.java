package org.delarosa.app.modules.paciente.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.modules.paciente.enums.TipoSangre;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorialMedico;

    @Column(nullable = false)
    private Double estatura;

    @Column(nullable = false)
    private Double peso;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoSangre tipoSangre;

    @OneToOne
    @JoinColumn(name = "idPaciente", nullable = false)
    private Paciente paciente;

}
