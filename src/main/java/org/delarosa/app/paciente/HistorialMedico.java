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
public class HistorialMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorialMedico;

    private Double estatura;
    private Double peso;

    @Enumerated(EnumType.STRING)
    private TipoSangre tipoSangre;
    
    @OneToOne
    @JoinColumn(name = "idPaciente",nullable = false)
    private Paciente paciente;


}
