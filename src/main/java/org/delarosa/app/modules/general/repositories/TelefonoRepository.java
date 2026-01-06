package org.delarosa.app.modules.general.repositories;

import org.delarosa.app.modules.general.entities.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelefonoRepository extends JpaRepository<Telefono, Integer> {
    Telefono findByNumeroTelefono(String numeroTelefono);
}
