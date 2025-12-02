package org.delarosa.app.modules.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.modules.general.entities.Persona;
import org.delarosa.app.modules.paciente.entities.Alergia;
import org.delarosa.app.modules.paciente.entities.HistorialMedico;

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
    private List<PacientePadecimiento> padecimientos;

    @ManyToMany
    @JoinTable(name = "AlergiaPaciente",
            joinColumns = @JoinColumn(name = "idPaciente"),
            inverseJoinColumns = @JoinColumn(name = "idAlergia"))
    private List<Alergia> alergias;
    
    @OneToOne(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private HistorialMedico historialMedico;

}
