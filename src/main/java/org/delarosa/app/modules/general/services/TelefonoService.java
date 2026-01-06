package org.delarosa.app.modules.general.services;

import org.delarosa.app.modules.general.entities.Telefono;

public interface TelefonoService {
    Telefono buscarOCrearTelefono(String numero);
}
