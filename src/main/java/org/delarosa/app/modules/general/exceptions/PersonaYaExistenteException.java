package org.delarosa.app.modules.general.exceptions;

public class PersonaYaExistenteException extends RuntimeException {
    public PersonaYaExistenteException(String message) {
        super(message);
    }
}
