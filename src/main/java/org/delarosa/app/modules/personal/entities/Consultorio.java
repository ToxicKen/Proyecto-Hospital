package org.delarosa.app.modules.personal.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsultorio;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @OneToMany(
            mappedBy = "consultorio",
            fetch = FetchType.LAZY
    )
    private List<Doctor> doctores = new ArrayList<>();

    public void agregarDoctor(Doctor doctor) {
        doctores.add(doctor);
        doctor.setConsultorio(this);
    }

    public void removerDoctor(Doctor doctor) {
        doctores.remove(doctor);
        doctor.setConsultorio(null);
    }

}
