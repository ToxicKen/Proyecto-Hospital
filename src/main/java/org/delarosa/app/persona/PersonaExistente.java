package org.delarosa.app.persona;

public class PersonaExistente extends RuntimeException {
    public PersonaExistente(String message) {
        super(message);
    }
}
