package org.delarosa.app.doctor;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    @Id
    private Integer idDoctor;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idDoctor",referencedColumnName = "idEmpleado")
    private Empleado empleado;
}
