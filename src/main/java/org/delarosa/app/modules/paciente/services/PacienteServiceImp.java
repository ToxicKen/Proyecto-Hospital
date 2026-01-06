package org.delarosa.app.modules.paciente.services;

import lombok.RequiredArgsConstructor;

import org.delarosa.app.modules.general.dtos.ActualizarPacienteRequest;
import org.delarosa.app.modules.general.dtos.PadecimientoRequest;
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
import org.springframework.security.core.parameters.P;
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

        if (paciente.getPersona() == null) {
            throw new RuntimeException("El paciente no tiene información personal asociada");
        }

        if (paciente.getPersona().getUsuario() == null) {
            throw new RuntimeException("El paciente no tiene usuario asociado");
        }

        // Actualizar datos de persona
        actualizarDatosPersona(paciente.getPersona(), request);

        // Actualizar email de usuario
        paciente.getPersona().getUsuario().setCorreoElectronico(request.email());

        // Actualizar historial médico
        actualizarHistorialMedico(paciente, request);

        // Actualizar alergias
        actualizarAlergias(paciente, request.alergias());

        // Actualizar padecimientos
        actualizarPadecimientos(paciente, request.padecimientos());

        paciente = pacienteRepo.save(paciente);
        return mapearPaciente(paciente);
    }

    private void actualizarDatosPersona(Persona persona, ActualizarPacienteRequest request) {
        persona.setNombre(request.nombre());
        persona.setApellidoP(request.apellidoP());
        persona.setApellidoM(request.apellidoM());
        persona.setCurp(request.curp());

        // Actualizar dirección (campos separados)
        persona.setCalle(request.calle() != null ? request.calle() : "");
        persona.setColonia(request.colonia() != null ? request.colonia() : "");
        persona.setNumero(request.numero() != null ? request.numero() : "");

        // Actualizar teléfonos (a través de PersonaTelefono)
        actualizarTelefonos(persona, request.telefonos());
    }

    private void actualizarTelefonos(Persona persona, List<TelefonoDTO> telefonosDto) {
        // Limpiar relaciones existentes
        persona.getTelefonos().clear();

        if (telefonosDto != null && !telefonosDto.isEmpty()) {
            int i = 1;
            for (TelefonoDTO telDto : telefonosDto) {
                if (telDto.numero() != null && !telDto.numero().trim().isEmpty()) {
                    // Crear o buscar teléfono
                    Telefono telefono = telefonoService.buscarOCrearTelefono(telDto.numero());

                    // Crear relación PersonaTelefono
                    PersonaTelefono personaTelefono = PersonaTelefono.builder()
                            .persona(persona)
                            .telefono(telefono)
                            .tipo(telDto.tipo() != null ? telDto.tipo() : "movil")
                            .build();

                    persona.addTelefono(personaTelefono);
                }
            }
        }
    }

    private void actualizarHistorialMedico(Paciente paciente, ActualizarPacienteRequest request) {
        HistorialMedico historial = paciente.getHistorialMedico();

        if (historial == null) {
            historial = new HistorialMedico();
            historial.setPaciente(paciente); // ¡IMPORTANTE!
            paciente.setHistorialMedico(historial);
        }

        historial.setPeso(request.peso());
        historial.setEstatura(request.estatura());

        if (request.tipoSangre() != null && !request.tipoSangre().isEmpty()) {
            try {
                historial.setTipoSangre(TipoSangre.valueOf(request.tipoSangre()));
            } catch (IllegalArgumentException e) {
                historial.setTipoSangre(null);
            }
        } else {
            historial.setTipoSangre(null);
        }
    }

    private void actualizarAlergias(Paciente paciente, List<String> alergiasNombres) {
        paciente.getAlergias().clear();

        if (alergiasNombres != null && !alergiasNombres.isEmpty()) {
            for (String nombreAlergia : alergiasNombres) {
                if (nombreAlergia != null && !nombreAlergia.trim().isEmpty()) {
                    // Buscar alergia existente o crear nueva
                    Alergia alergia = alergiaRepo.findByNombre(nombreAlergia.trim())
                            .orElseGet(() -> {
                                Alergia nueva = new Alergia();
                                nueva.setNombre(nombreAlergia.trim());
                                return alergiaRepo.save(nueva);
                            });

                    paciente.getAlergias().add(alergia);
                }
            }
        }
    }

    private void actualizarPadecimientos(Paciente paciente, List<PadecimientoRequest> padecimientosReq) {
        // Limpiar relaciones existentes
        if (paciente.getPadecimientos() != null) {
            paciente.getPadecimientos().clear();
        }

        if (padecimientosReq != null && !padecimientosReq.isEmpty()) {
            for (PadecimientoRequest padecimientoReq : padecimientosReq) {
                String nombre = padecimientoReq.nombre();
                if (nombre != null && !nombre.trim().isEmpty()) {
                    String nombreLimpio = nombre.trim();

                    // Buscar padecimiento
                    Optional<Padecimiento> padecimientoOpt = padecimientoRepo.findByNombre(nombreLimpio);
                    Padecimiento padecimiento;

                    if (padecimientoOpt.isPresent()) {
                        padecimiento = padecimientoOpt.get();
                    } else {
                        // Crear nuevo padecimiento
                        padecimiento = new Padecimiento();
                        padecimiento.setNombre(nombreLimpio);
                        padecimiento = padecimientoRepo.save(padecimiento);
                    }

                    // Crear ID compuesto
                    PacientePadecimientoId id = new PacientePadecimientoId();
                    id.setIdPaciente(paciente.getIdPaciente());
                    id.setIdPadecimiento(padecimiento.getIdPadecimiento());

                    // Crear relación
                    PacientePadecimiento relacion = new PacientePadecimiento();
                    relacion.setIdPadecimientoPaciente(id);
                    relacion.setPaciente(paciente);
                    relacion.setPadecimiento(padecimiento);

                    String descripcion = padecimientoReq.descripcion();
                    relacion.setDescripcion(descripcion != null ? descripcion.trim() : "");

                    // Inicializar la lista si es null
                    if (paciente.getPadecimientos() == null) {
                        paciente.setPadecimientos(new ArrayList<>());
                    }

                    paciente.getPadecimientos().add(relacion);
                }
            }
        }
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
