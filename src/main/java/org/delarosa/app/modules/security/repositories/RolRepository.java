package org.delarosa.app.modules.security.repositories;

import org.delarosa.app.modules.security.entities.Rol;
import org.delarosa.app.modules.security.enums.NombreRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol,Integer> {
    Optional<Rol> findByNombre(NombreRol nombre);
}
