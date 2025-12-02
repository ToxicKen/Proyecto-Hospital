package org.delarosa.app.modules.paciente;

import lombok.RequiredArgsConstructor;

import org.delarosa.app.modules.general.services.PersonaService;
import org.delarosa.app.modules.paciente.dtos.*;
import org.delarosa.app.modules.paciente.entities.*;
import org.delarosa.app.modules.paciente.exceptions.AlergiaNoEncontradaException;
import org.delarosa.app.modules.paciente.exceptions.PacienteNoEncontradoException;
import org.delarosa.app.modules.paciente.exceptions.PadecimientoNoEncontrado;
import org.delarosa.app.modules.paciente.repositories.AlergiaRepository;
import org.delarosa.app.modules.paciente.repositories.PacienteRepository;
import org.delarosa.app.modules.paciente.repositories.PadecimentoRepository;
import org.delarosa.app.modules.paciente.services.PacienteService;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteServiceImp implements PacienteService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PersonaService personaService;
    private final PacienteRepository pacienteRepo;
    private final AlergiaRepository alergiaRepo;
    private final PadecimentoRepository padecimientoRepo;

    // --- RegistrarPaciente ---

    @Override
    @Transactional
    public AuthResponse registrarPaciente(RegistroPacienteRequest registroPacienteRequest) {
        Paciente paciente = crearPaciente(registroPacienteRequest);
        return crearAuthResponse(paciente);
    }

    // --- Crear Paciente ---

    @Override
    @Transactional
    public Paciente crearPaciente(RegistroPacienteRequest registroPacienteRequest) {
        Usuario usuarioCreado = usuarioService.crearUsuario(
                registroPacienteRequest.registroPersonaRequest(),
                registroPacienteRequest.registroUsuarioRequest()
        );

        asignarRolPaciente(usuarioCreado);

        Paciente nvoPaciente = Paciente.builder()
                .persona(usuarioCreado.getPersona())
                .build();
        agregarAlergias(nvoPaciente, registroPacienteRequest);
        agregarPadecimientos(nvoPaciente, registroPacienteRequest.padecimientos());
        nvoPaciente.setHistorialMedico(crearHistorialMedico(nvoPaciente, registroPacienteRequest.historialMedico()));
        return pacienteRepo.save(nvoPaciente);
    }


    private void asignarRolPaciente(Usuario usuario) {
        usuarioService.addRolPaciente(usuario);
    }

    private void agregarAlergias(Paciente paciente, RegistroPacienteRequest dto) {
        agregarAlergiasExistentes(paciente, dto);
        agregarAlergiasNoExistentes(paciente, dto);
    }

    private void agregarAlergiasExistentes(Paciente paciente, RegistroPacienteRequest dto) {
        for (Integer idAlergia : dto.idAlergias()) {
            agregarAlergiaAPaciente(paciente, obtenerAlergiaPorId(idAlergia));
        }
    }

    private void agregarAlergiasNoExistentes(Paciente paciente, RegistroPacienteRequest dto) {
        for (String nombre : dto.nuevasAlergias()) {
            agregarAlergiaAPaciente(paciente, crearNuevaAlergia(nombre));
        }
    }

    private void agregarAlergiaAPaciente(Paciente paciente, Alergia alergia) {
        paciente.getAlergias().add(alergia);
    }

    private Alergia obtenerAlergiaPorId(Integer id) {
        return alergiaRepo.findById(id)
                .orElseThrow(() -> new AlergiaNoEncontradaException("Alergia con id " + id + " no existente"));
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

        PacientePadecimientoId id = new PacientePadecimientoId(
                paciente.getIdPaciente(),
                padecimiento.getIdPadecimiento()
        );

        return PacientePadecimiento.builder()
                .idPadecimientoPaciente(id)
                .paciente(paciente)
                .padecimiento(padecimiento)
                .descripcion(dto.descripcion())
                .build();
    }

    private Padecimiento obtenerPadecimientoPorId(Integer id) {
        return padecimientoRepo.findById(id).orElseThrow(() -> new PadecimientoNoEncontrado("Padecimiento con id: " + id + " no existente"));
    }

    private Padecimiento crearNuevoPadecimiento(String nombre) {
        Padecimiento padecimiento = new Padecimiento();
        padecimiento.setNombre(nombre);
        return padecimientoRepo.save(padecimiento);
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


    private AuthResponse crearAuthResponse(Paciente paciente) {
        Usuario usuario = paciente.getPersona().getUsuario();
        String token = jwtService.getToken(usuario);
        return AuthResponse.builder().token(jwtService.getToken(usuario)).build();

    }



    public PacienteResponse mapearPaciente(Paciente paciente) {
        return new PacienteResponse(personaService.obetenerResponsePersona(paciente.getPersona())
                , paciente.getPersona().getUsuario().getCorreoElectronico(),
                mapearAlergia(paciente),
                mapearPadecimientosDTO(paciente),
                mapearHistorialMedicoDTO(paciente)
        );
    }

    @Override
    public PacienteResponse obtenerDatosPaciente(String token) {
        return mapearPaciente(obtenerPacienteDesdeToken(token));
    }

    @Override
    public Paciente obtenerPacienteDesdeToken(String token) {
        return pacienteRepo.findById(jwtService.getUserIdFromToken(token)).orElseThrow(() -> new PacienteNoEncontradoException("Paciente no existente"));
    }

    private List<String> mapearAlergia(Paciente paciente) {
        return paciente.getAlergias().stream().map(Alergia::getNombre).toList();
    }

    private HistorialMedicoDTO mapearHistorialMedicoDTO(Paciente paciente) {
        return new HistorialMedicoDTO(paciente.getHistorialMedico().getPeso(), paciente.getHistorialMedico().getEstatura(), paciente.getHistorialMedico().getTipoSangre().name());
    }

    private List<PadecimientoDatosDTO> mapearPadecimientosDTO(Paciente paciente) {
        return paciente.getPadecimientos().stream().map(p -> new PadecimientoDatosDTO(p.getPadecimiento().getNombre(), p.getDescripcion())).toList();
    }

    @Override
    public Paciente obtenerPacienteById(Integer id) {
        return pacienteRepo.findById(id).orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado"));
    }

    @Override
    public Paciente buscarPorCorreo(String email) {
        return pacienteRepo.buscarPorEmailDeUsuario(email)
                .orElseThrow(() -> new RuntimeException("No se encontró un Paciente vinculado al usuario: " + email));
    }
}
