package org.delarosa.app.modules.paciente.services;

import lombok.RequiredArgsConstructor;

import org.delarosa.app.modules.general.dtos.PadecimientoRequest;
import org.delarosa.app.modules.paciente.dtos.ActualizarPacienteRequest;
import org.delarosa.app.modules.general.dtos.TelefonoDTO;
import org.delarosa.app.modules.general.entities.Persona;
import org.delarosa.app.modules.general.entities.PersonaTelefono;
import org.delarosa.app.modules.general.entities.Telefono;
import org.delarosa.app.modules.general.services.PersonaService;
import org.delarosa.app.modules.general.services.TelefonoService;
import org.delarosa.app.modules.paciente.dtos.*;
import org.delarosa.app.modules.paciente.entities.*;
import org.delarosa.app.modules.paciente.enums.TipoSangre;
import org.delarosa.app.modules.paciente.exceptions.AlergiaNoEncontradaException;
import org.delarosa.app.modules.paciente.exceptions.PacienteNoEncontradoException;
import org.delarosa.app.modules.paciente.exceptions.PadecimientoNoEncontradoException;
import org.delarosa.app.modules.paciente.repositories.AlergiaRepository;
import org.delarosa.app.modules.paciente.repositories.PacienteRepository;
import org.delarosa.app.modules.paciente.repositories.PadecimentoRepository;
import org.delarosa.app.modules.security.dto.AuthResponse;
import org.delarosa.app.modules.security.jwt.JwtService;
import org.delarosa.app.modules.security.entities.Usuario;
import org.delarosa.app.modules.security.services.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacienteServiceImp implements PacienteService {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final PersonaService personaService;
    private final PacienteRepository pacienteRepo;
    private final AlergiaRepository alergiaRepo;
    private final PadecimentoRepository padecimientoRepo;
    private final TelefonoService telefonoService;

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
        nvoPaciente = pacienteRepo.save(nvoPaciente);
        System.out.println(nvoPaciente.getIdPaciente());
        agregarPadecimientos(nvoPaciente, registroPacienteRequest.padecimientos());
        nvoPaciente.setHistorialMedico(crearHistorialMedico(nvoPaciente, registroPacienteRequest.historialMedico()));
        return pacienteRepo.save(nvoPaciente);
    }

    // --- Obtener Datos de Paciente desde Token ---

    @Override
    public PacienteResponse obtenerDatosPaciente(String correo) {
        return mapearPaciente(obtenerPacienteByCorreo(correo));
    }

    // --- Obtener Entidad Paciente desde Token---

    @Override
    public Paciente obtenerPacienteDesdeToken(String token) {
        return pacienteRepo.findById(jwtService.getUserIdFromToken(token)).orElseThrow(() -> new PacienteNoEncontradoException("Paciente no existente"));
    }

    // --- Obtener Entidad Paciente desde Id---

    @Override
    public Paciente obtenerPacienteById(Integer id) {
        return pacienteRepo.findById(id).orElseThrow(() -> new PacienteNoEncontradoException("Paciente no encontrado"));
    }

    // --- Obtener Entidad Paciente desde Correo Electronico---

    @Override
    public Paciente obtenerPacienteByCorreo(String email) {
        return pacienteRepo.buscarPorEmailDeUsuario(email)
                .orElseThrow(() -> new RuntimeException("No se encontró un Paciente vinculado al usuario: " + email));
    }

    // --- Obtener Todos los padecimientos ya existentes en el sistema ---
    @Override
    public List<PadecimientoExistenteDTO> obtenerPadecimientosExistentes() {
        return padecimientoRepo.findAll()
                .stream()
                .map(this::mapearPadecimiento)
                .toList();
    }

    // --- Obtener Todos los padecimientos ya existentes en el sistema ---

    @Override
    public List<AlergiaExistenteDTO> obtenerAlergiasExistentes() {
        return alergiaRepo.findAll().stream().map(a->new AlergiaExistenteDTO(a.getIdAlergia(),a.getNombre())).toList();
    }

    @Override
    @Transactional
    public PacienteResponse actualizarPaciente(String correo, ActualizarPacienteRequest request) {

        Paciente paciente = obtenerPacienteByCorreo(correo);

        validarPaciente(paciente);

        actualizarDatosPersona(paciente.getPersona(), request);

        actualizarUsuario(paciente.getPersona().getUsuario(), request);

        actualizarHistorialMedico(paciente, request);

        actualizarAlergias(paciente, request);

        pacienteRepo.save(paciente);

        return mapearPaciente(paciente);
    }


    private void validarPaciente(Paciente paciente) {
        if (paciente.getPersona() == null) {
            throw new IllegalStateException("El paciente no tiene información personal asociada");
        }

        if (paciente.getPersona().getUsuario() == null) {
            throw new IllegalStateException("El paciente no tiene usuario asociado");
        }
    }

    private void actualizarUsuario(Usuario usuario, ActualizarPacienteRequest request) {
        if (request.email() != null && !request.email().isBlank()) {
            usuario.setCorreoElectronico(request.email());
        }
    }


    private void actualizarDatosPersona(Persona persona, ActualizarPacienteRequest request) {

        persona.setNombre(request.nombre());
        persona.setApellidoP(request.apellidoP());
        persona.setApellidoM(request.apellidoM());
        persona.setCurp(request.curp());

        persona.setCalle(valorSeguro(request.calle()));
        persona.setColonia(valorSeguro(request.colonia()));
        persona.setNumero(valorSeguro(request.numero()));

        actualizarTelefonos(persona, request.telefonos());
    }

    private String valorSeguro(String valor) {
        return valor != null ? valor.trim() : "";
    }


    private void actualizarTelefonos(Persona persona, List<TelefonoDTO> telefonosDto) {

        persona.getTelefonos().clear();

        if (telefonosDto == null || telefonosDto.isEmpty()) {
            return;
        }

        for (TelefonoDTO telDto : telefonosDto) {

            if (telDto.numero() == null || telDto.numero().isBlank()) {
                continue;
            }

            Telefono telefono = telefonoService.buscarOCrearTelefono(telDto.numero());

            PersonaTelefono personaTelefono = PersonaTelefono.builder()
                    .persona(persona)
                    .telefono(telefono)
                    .tipo(telDto.tipo() != null ? telDto.tipo() : "MOVIL")
                    .build();

            persona.addTelefono(personaTelefono);
        }
    }


    private void actualizarHistorialMedico(Paciente paciente, ActualizarPacienteRequest request) {

        HistorialMedico historial = paciente.getHistorialMedico();

        if (historial == null) {
            historial = new HistorialMedico();
            historial.setPaciente(paciente);
            paciente.setHistorialMedico(historial);
        }

        historial.setPeso(request.peso());
        historial.setEstatura(request.estatura());

        if (request.tipoSangre() != null && !request.tipoSangre().isBlank()) {
            try {
                historial.setTipoSangre(TipoSangre.valueOf(request.tipoSangre()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Tipo de sangre inválido");
            }
        } else {
            historial.setTipoSangre(null);
        }
    }


    private void actualizarAlergias(Paciente paciente, ActualizarPacienteRequest dto) {

        paciente.getAlergias().clear();

        if (dto.idAlergias() != null) {
            for (Integer idAlergia : dto.idAlergias()) {
                Alergia alergia = alergiaRepo.findById(idAlergia)
                        .orElseThrow(() -> new AlergiaNoEncontradaException("Alergia no encontrada"));
                paciente.getAlergias().add(alergia);
            }
        }

        if (dto.nuevasAlergias() != null) {
            for (String nombreAlergia : dto.nuevasAlergias()) {
                paciente.getAlergias().add(crearNuevaAlergia(nombreAlergia));
            }
        }
    }


    private void actualizarPadecimientos(
            Paciente paciente,
            List<PadecimientoRequest> padecimientosReq
    ) {

        // Limpiar relaciones existentes (orphanRemoval se encarga del delete)
        paciente.getPadecimientos().clear();

        if (padecimientosReq == null || padecimientosReq.isEmpty()) {
            return;
        }

        for (PadecimientoRequest req : padecimientosReq) {

            if (req.nombre() == null || req.nombre().isBlank()) {
                continue;
            }

            Padecimiento padecimiento = obtenerOCrearPadecimiento(req.nombre().trim());

            PacientePadecimiento relacion = new PacientePadecimiento();
            relacion.setPaciente(paciente);
            relacion.setPadecimiento(padecimiento);
            relacion.setDescripcion(
                    req.descripcion() != null ? req.descripcion().trim() : ""
            );

            // ID compuesto
            PacientePadecimientoId id = new PacientePadecimientoId(
                    paciente.getIdPaciente(),
                    padecimiento.getIdPadecimiento()
            );
            relacion.setIdPadecimientoPaciente(id);

            paciente.getPadecimientos().add(relacion);
        }
    }

    private Padecimiento obtenerOCrearPadecimiento(String nombre) {

        return padecimientoRepo.findByNombre(nombre)
                .orElseGet(() -> {
                    Padecimiento nuevo = new Padecimiento();
                    nuevo.setNombre(nombre);
                    return padecimientoRepo.save(nuevo);
                });
    }


    // --- Metodos de apoyo---

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
        System.out.println(paciente.getIdPaciente());
        PacientePadecimientoId id = new PacientePadecimientoId();
        id.setIdPaciente(paciente.getIdPaciente());
        id.setIdPadecimiento(padecimiento.getIdPadecimiento());
        return PacientePadecimiento.builder()
                .idPadecimientoPaciente(id)
                .paciente(paciente)
                .padecimiento(padecimiento)
                .descripcion(dto.descripcion())
                .build();
    }

    private Padecimiento obtenerPadecimientoPorId(Integer id) {
        return padecimientoRepo.findById(id).orElseThrow(() -> new PadecimientoNoEncontradoException("Padecimiento con id: " + id + " no existente"));
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
        return new PacienteResponse(personaService.obtenerResponsePersona(paciente.getPersona())
                , paciente.getPersona().getUsuario().getCorreoElectronico(),
                mapearAlergia(paciente),
                mapearPadecimientosDTO(paciente),
                mapearHistorialMedicoDTO(paciente)
        );
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

    private PadecimientoExistenteDTO mapearPadecimiento(Padecimiento padecimiento) {
        return new PadecimientoExistenteDTO(padecimiento.getIdPadecimiento(), padecimiento.getNombre());
    }
}
