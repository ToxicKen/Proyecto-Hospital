package org.delarosa.app.modules.personal.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recepcionista {

    @Id
    private Integer idRecepcionista;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idRecepcionista", referencedColumnName = "idEmpleado")
    private Empleado empleado;

}
