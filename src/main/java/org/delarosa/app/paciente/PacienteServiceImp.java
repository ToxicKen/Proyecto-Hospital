package org.delarosa.app.paciente;

import lombok.RequiredArgsConstructor;

import org.delarosa.app.persona.PersonaDTO;
import org.delarosa.app.security.auth.AuthResponse;
import org.delarosa.app.usuario.Usuario;
import org.delarosa.app.usuario.UsuarioService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteServiceImp implements PacienteService {
    private final UsuarioService usuarioService;
    private final PacienteRepository pacienteRepo;
    private final AlergiaRepository alergiaRepo;
    private final PadecimentoRepository padecimentoRepo;


    @Transactional
    @Override
    public Paciente crearPaciente(PacienteDTO pacienteDTO) {
        Paciente nvoPaciente = Paciente.builder()
                .persona(usuarioService.crearUsuario(pacienteDTO.personaDTO(), pacienteDTO.usuarioDTO()).getPersona())
                .build();
        agregarAlergias(nvoPaciente, pacienteDTO);
        agregarPadecimientos(nvoPaciente, pacienteDTO.padecimientos());
        crearHistorialMedico(nvoPaciente,pacienteDTO.historialMedico());
        return pacienteRepo.save(nvoPaciente);
    }

    private void agregarAlergias(Paciente paciente, PacienteDTO dto) {
        agregarAlergiasExistentes(paciente, dto);
        agregarAlergiasNoExistentes(paciente, dto);
    }

    private void agregarAlergiasExistentes(Paciente paciente, PacienteDTO dto) {
        for (Integer idAlergia : dto.idAlergias()) {
            agregarAlergiaAPaciente(paciente, obtenerAlergiaPorId(idAlergia));
        }
    }

    private void agregarAlergiasNoExistentes(Paciente paciente, PacienteDTO dto) {
        for (String nombre : dto.nuevasAlergias()) {
            agregarAlergiaAPaciente(paciente, crearNuevaAlergia(nombre));
        }
    }

    private void agregarAlergiaAPaciente(Paciente paciente, Alergia alergia) {
        paciente.getAlergias().add(alergia);
    }

    private Alergia obtenerAlergiaPorId(Integer id) {
        return alergiaRepo.findById(id)
                .orElseThrow(() -> new AlergiaNoExistente("Alergia con id " + id + " no existente"));
    }

    private Alergia crearNuevaAlergia(String nombre) {
        Alergia alergia = new Alergia();
        alergia.setNombre(nombre);
        return alergiaRepo.save(alergia);
    }

    private void agregarPadecimientos(Paciente paciente, List<PadecimientoDTO> padecimientos) {
        for (PadecimientoDTO dto : padecimientos) {
            PacientePadecimiento relacion = crearPacientePadecimiento(paciente, dto);
            paciente.getPadecimientos().add(relacion);
        }
    }

    private Padecimiento obtenerOPersistirPadecimiento(PadecimientoDTO dto) {
        if (dto.id() != null) {
            return obtenerPadecimientoPorId(dto.id());
        }
        return crearNuevoPadecimiento(dto.nombre());
    }

    private PacientePadecimiento crearPacientePadecimiento(Paciente paciente, PadecimientoDTO dto) {
        Padecimiento padecimiento = obtenerOPersistirPadecimiento(dto);
        return PacientePadecimiento.builder()
                .paciente(paciente)
                .padecimiento(padecimiento)
                .descripcion(dto.descripcion())
                .build();
    }

    private Padecimiento obtenerPadecimientoPorId(Integer id) {
        return padecimentoRepo.findById(id).orElseThrow(() -> new PadecimientoNoExistente("Padecimiento con id: " + id + " no existente"));
    }

    private Padecimiento crearNuevoPadecimiento(String nombre) {
        Padecimiento padecimiento = new Padecimiento();
        padecimiento.setNombre(nombre);
        return padecimentoRepo.save(padecimiento);
    }

    private HistorialMedico crearHistorialMedico(Paciente paciente, HistorialMedicoDTO dto) {
        return HistorialMedico.builder()
                .estatura(dto.estatura())
                .peso(dto.peso())
                .paciente(paciente)
                .tipoSangre(convertirATipoSangre(dto.tipoSangre()))
                .build();
    }

    private TipoSangre convertirATipoSangre(String tipoSangre) {
        return TipoSangre.valueOf(tipoSangre);
    }



    @Override
public AuthResponse registrarPaciente(Paciente paciente) {



}
}
