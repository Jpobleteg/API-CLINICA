package cl.duoc.Api_farmacia.Repository;

import cl.duoc.pharmacy_service.Model.RecetaInsumoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

public class FarmaciaRepository {

    @Repository
    public interface RecetaInsumoRepository<RecetaInsumoModel> extends JpaRepository<RecetaInsumoModel, Long> {

        // Método para consultar qué insumos están pendientes por paciente
        List<RecetaInsumoModel> findByPacienteId(Long pacienteId);

        // Buscar solicitudes filtradas por su estado actual
        List<RecetaInsumoModel> findByEstado(String estado);
    }

}
