package org.delarosa.app.modules.personal.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecialidad;

    @Column(nullable = false,unique = true)
    private String nombre;

    @Column(precision =  19, scale = 2,nullable = false)
    private BigDecimal costo;

    @OneToMany(mappedBy = "especialidad")
    private List<Doctor> doctores = new ArrayList<>();

    public void agregarDoctor(Doctor doctor) {
        if (doctores == null) {
            doctores = new ArrayList<>();
        }
        doctores.add(doctor);
        doctor.setEspecialidad(this);
    }

}
