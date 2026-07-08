package cl.duoc.Api_clinica.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.Api_clinica.Model.ClinicaModel; 

public interface ClinicaRepository extends JpaRepository<ClinicaModel, Long> { 
}
