package org.delarosa.app.persona;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaServiceImp implements  PersonaService{
    private final PersonaRepository personaRepo;

    @Transactional
    @Override
    public Persona crearPersona(PersonaDTO personaDTO) {
        validarPersonaExistente(personaDTO);
        Persona  personaCreada = crearEntidadPersona(personaDTO);
        agregarTelefonos(personaCreada,personaDTO);
        return personaRepo.save(personaCreada);
    }

    private Persona crearEntidadPersona(PersonaDTO personaDTO) {
        return   Persona.builder()
                .nombre(personaDTO.nombre())
                .apellidoP(personaDTO.apellidoP())
                .apellidoM(personaDTO.apellidoM())
                .calle(personaDTO.calle())
                .colonia(personaDTO.colonia())
                .numero(personaDTO.numero())
                .curp(personaDTO.curp())
                .build();
    }

    private void agregarTelefonos(Persona persona,PersonaDTO personaDTO) {
        List<PersonaTelefono> telefonos = personaDTO.telefonos().stream().map(t-> crearPersonaTelefono(persona,t)).toList();
        persona.setTelefonos(telefonos);
    }


    private Telefono crearTelefono(TelefonoDTO telefono) {
        Telefono nvoTel =  new Telefono();
        nvoTel.setNumeroTelefono(telefono.numero());
        return nvoTel;
    }

    private PersonaTelefono crearPersonaTelefono(Persona persona, TelefonoDTO telefonoDTO) {
        return PersonaTelefono.builder()
                .persona(persona)
                .telefono(crearTelefono(telefonoDTO))
                .tipo(telefonoDTO.tipo())
                .build();
    }

    private void validarPersonaExistente(PersonaDTO personaDTO) {
        if(personaRepo.existsByCurp(personaDTO.curp())){
            throw new PersonaExistente("Persona existente");
        }
    }
}
