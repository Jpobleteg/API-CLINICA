package cl.duoc.gestion_camas.Repository;

import cl.duoc.gestion_camas.Model.pabellonModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface pabellonRepository extends JpaRepository<pabellonModel, Long> {

    @Transactional
    Optional<pabellonModel> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<pabellonModel> findByActivo(int activo);
}
