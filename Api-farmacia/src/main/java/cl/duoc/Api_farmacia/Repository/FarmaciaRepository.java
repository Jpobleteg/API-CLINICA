package cl.duoc.Api_farmacia.Repository;

import cl.duoc.pharmacy_service.Model.RecetaInsumoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FarmaciaRepository<FarmaciaRepository> extends JpaRepository<FarmaciaRepository, Long> {

        // Método para consultar qué insumos están pendientes por paciente
        List<FarmaciaRepository> findByPacienteId(Long pacienteId);

        // Buscar solicitudes filtradas por su estado actual
        List<FarmaciaRepository> findByEstado(String estado);
    }
