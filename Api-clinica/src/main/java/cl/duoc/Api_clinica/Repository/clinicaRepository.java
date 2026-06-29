package cl.duoc.Api_clinica.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.Api_clinica.Model.clinicaModel;

public interface clinicaRepository extends JpaRepository<clinicaModel, Long> {
}
