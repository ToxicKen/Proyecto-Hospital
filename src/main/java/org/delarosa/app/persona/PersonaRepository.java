package org.delarosa.app.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

    boolean existsByCurp(String curp);

    @Query(value = "SELECT CONCAT(CONCAT(p.nombre, ' '), CONCAT(p.apellidoM, ' '), p.apellidoP) FROM  Persona p Where p.idPersona =:idPersona ")
    Optional<String> getNombreCompletoPersona(@Param("idPersona") Integer idPersona);
}
