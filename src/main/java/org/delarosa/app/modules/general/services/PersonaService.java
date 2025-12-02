package org.delarosa.app.modules.general.services;

import org.delarosa.app.modules.general.dtos.RegistroPersonaRequest;
import org.delarosa.app.modules.general.entities.Persona;

public interface PersonaService {
    Persona crearPersona(RegistroPersonaRequest registroPersonaRequest);

    RegistroPersonaRequest mapearPersona(Persona persona);

    String obtenerNombreCompletoPersona(Persona persona);
}
