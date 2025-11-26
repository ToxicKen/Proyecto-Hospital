package org.delarosa.app.empleado;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.delarosa.app.persona.Persona;

import java.math.BigDecimal;

@Entity
@Getter
@Setter

public class Empleado {
    @Id
    private Integer idEmpleado;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idEmpleado",referencedColumnName = "idPersona")
    private Persona persona;

    @Column(precision =  19,scale = 2)
    private BigDecimal salario;
}
