package org.delarosa.app.modules.clinico.repositories;

import org.delarosa.app.modules.clinico.entities.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta,Integer>
{
}
