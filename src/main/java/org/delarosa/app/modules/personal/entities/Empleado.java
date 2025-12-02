package org.delarosa.app.modules.personal.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.delarosa.app.modules.general.entities.Persona;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
public class Empleado {

    @Id
    private Integer idEmpleado;

    @Column(precision = 19, scale = 2, nullable = false)
    private BigDecimal salario;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idEmpleado", referencedColumnName = "idPersona")
    private Persona persona;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioEmpleado> horarios ;

}
