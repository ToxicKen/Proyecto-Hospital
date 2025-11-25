package org.delarosa.app.paciente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PadecimentoRepository extends JpaRepository<Padecimiento, Integer> {
}
