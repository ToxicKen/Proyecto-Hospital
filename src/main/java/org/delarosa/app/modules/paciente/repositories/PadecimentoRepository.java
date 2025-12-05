package org.delarosa.app.modules.paciente.repositories;
import org.delarosa.app.modules.paciente.entities.Padecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadecimentoRepository extends JpaRepository<Padecimiento, Integer> {
}
