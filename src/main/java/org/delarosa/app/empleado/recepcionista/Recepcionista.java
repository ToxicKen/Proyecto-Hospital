package org.delarosa.app.empleado.recepcionista;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.delarosa.app.empleado.Empleado;
import org.delarosa.app.empleado.EmpleadoDTO;

@Entity
@Getter
@Setter
public class Recepcionista {
    @Id
    private Integer idRecepcionista;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idRecepcionista",referencedColumnName = "idEmpleado")
    private Empleado empleado;


}
