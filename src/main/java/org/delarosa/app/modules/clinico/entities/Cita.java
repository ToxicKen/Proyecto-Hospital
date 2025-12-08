package org.delarosa.app.modules.clinico.entities;

import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.modules.clinico.enums.EstatusCita;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.personal.entities.Doctor;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer folioCita;

    @OneToOne
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente",nullable = false)
    private Paciente paciente;

    @OneToOne
    @JoinColumn(name = "idDoctor", referencedColumnName = "idDoctor",nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime fechaCita;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private EstatusCita estatus;

    @OneToOne(mappedBy = "cita",cascade = CascadeType.PERSIST,orphanRemoval = true)
    private OrdenPago ordenPago;

    public void asignarOrdenPago(OrdenPago orden) {
        this.ordenPago = orden;
        orden.setCita(this);
    }

}
