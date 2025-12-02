package org.delarosa.app.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.modules.general.entities.Persona;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paciente {
    @Id
    private Integer idPaciente;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPersona")
    private Persona persona;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PacientePadecimiento> padecimientos = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "AlergiaPaciente",
            joinColumns = @JoinColumn(name = "idPaciente"),
            inverseJoinColumns = @JoinColumn(name = "idAlergia"))
    @Builder.Default
    private List<Alergia> alergias = new ArrayList<>();


    @OneToOne(mappedBy = "paciente",cascade = CascadeType.ALL, orphanRemoval = true)
    private HistorialMedico historialMedico;

}
