package cl.duoc.gestion_camas.Repository;

import cl.duoc.gestion_camas.Model.camaModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface camaRepository extends JpaRepository<camaModel, Long> {

    @Transactional
    List<camaModel> findByPabellonId(Long pabellonId);

    @Transactional
    List<camaModel> findByEstado(String estado);

    // Busca camas DISPONIBLES compatibles con la prioridad del paciente
    // Consumida por camasService al responder a Api-clinica
    @Transactional
    @Query("SELECT c FROM camaModel c WHERE c.estado = 'DISPONIBLE' AND c.prioridadMin <= :prioridad ORDER BY c.prioridadMin ASC")
    List<camaModel> findDisponiblesPorPrioridad(@Param("prioridad") int prioridad);
}
