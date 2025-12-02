package org.delarosa.app.persona;

import lombok.RequiredArgsConstructor;
import org.delarosa.app.modules.general.dtos.RegistroPersonaRequest;
import org.delarosa.app.modules.general.entities.Persona;
import org.delarosa.app.modules.general.exceptions.PersonaYaExistenteException;
import org.delarosa.app.modules.general.repositories.PersonaRepository;
import org.delarosa.app.modules.general.services.PersonaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaServiceImp implements PersonaService {
    private final PersonaRepository personaRepo;

    // --- Creación y guardado de Persona ---

    @Transactional
    @Override
    public Persona crearPersona(RegistroPersonaRequest registroPersonaRequest) {
        validarPersonaExistente(registroPersonaRequest);
        Persona  personaCreada = crearEntidadPersona(registroPersonaRequest);
        agregarTelefonos(personaCreada, registroPersonaRequest);
        return personaRepo.save(personaCreada);
    }

    // --- Crear Response de Persona mapando una Persona a DTO ---

    @Override
    public RegistroPersonaRequest mapearPersona(Persona persona) {
        List<TelefonoDTO> telefonos = mapearTelefonos(persona);
        return new RegistroPersonaRequest(persona.getNombre(),
                persona.getApellidoM(),
                persona.getApellidoP(),
                persona.getCurp(),
                persona.getCalle(),persona.getColonia(),persona.getNumero(),telefonos);
    }



    private Persona crearEntidadPersona(RegistroPersonaRequest registroPersonaRequest) {
        return   Persona.builder()
                .nombre(registroPersonaRequest.nombre())
                .apellidoP(registroPersonaRequest.apellidoP())
                .apellidoM(registroPersonaRequest.apellidoM())
                .calle(registroPersonaRequest.calle())
                .colonia(registroPersonaRequest.colonia())
                .numero(registroPersonaRequest.numero())
                .curp(registroPersonaRequest.curp())
                .build();
    }

    private void agregarTelefonos(Persona persona, RegistroPersonaRequest registroPersonaRequest) {
        List<PersonaTelefono> telefonos = registroPersonaRequest.telefonos().stream().map(t-> crearPersonaTelefono(persona,t)).toList();
        persona.setTelefonos(telefonos);
    }


    private Telefono crearTelefono(TelefonoDTO telefono) {
        Telefono nvoTel =  new Telefono();
        nvoTel.setNumeroTelefono( telefono.numero());
        return nvoTel;
    }

    private PersonaTelefono crearPersonaTelefono(Persona persona, TelefonoDTO telefonoDTO) {
        return PersonaTelefono.builder()
                .persona(persona)
                .telefono(crearTelefono(telefonoDTO))
                .tipo(telefonoDTO.tipo())
                .build();
    }

    private void validarPersonaExistente(RegistroPersonaRequest registroPersonaRequest) {
        if(personaRepo.existsByCurp(registroPersonaRequest.curp())){
            throw new PersonaYaExistenteException("Persona existente");
        }
    }

    private List<TelefonoDTO> mapearTelefonos(Persona persona){
        return persona.getTelefonos()
                .stream()
                .map(tel -> new TelefonoDTO(tel.getTelefono().getNumeroTelefono(), tel.getTipo()))
                .toList();
    }


    @Override
    public String obtenerNombreCompletoPersona(Persona persona) {
        return personaRepo.getNombreCompletoPersona(persona.getIdPersona()).orElse("");
    }
}
