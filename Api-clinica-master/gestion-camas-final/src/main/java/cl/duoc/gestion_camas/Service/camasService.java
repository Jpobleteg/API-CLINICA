package cl.duoc.gestion_camas.Service;

import cl.duoc.gestion_camas.Model.camaDTO;
import cl.duoc.gestion_camas.Model.camaModel;
import cl.duoc.gestion_camas.Model.pabellonModel;
import cl.duoc.gestion_camas.Repository.camaRepository;
import cl.duoc.gestion_camas.Repository.pabellonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class camasService {

    @Autowired
    pabellonRepository pabellonRepo;

    @Autowired
    camaRepository camaRepo;


    // PABELLONES


    // READ ALL
    public List<pabellonModel> getAllPabellones() {
        return pabellonRepo.findByActivo(1);
    }

    // READ BY ID
    public Optional<pabellonModel> getPabellonById(Long id) {
        return pabellonRepo.findById(id);
    }

    // CREATE
    public pabellonModel createPabellon(pabellonModel pabellon) {
        pabellon.setActivo(1);
        return pabellonRepo.save(pabellon);
    }

    // UPDATE (PUT)
    public pabellonModel updatePabellon(Long id, pabellonModel datos) {
        Optional<pabellonModel> existente = pabellonRepo.findById(id);

        if (existente.isPresent()) {
            pabellonModel pabellon = existente.get();
            pabellon.setNombre(datos.getNombre());
            pabellon.setDescripcion(datos.getDescripcion());
            pabellon.setCapacidad(datos.getCapacidad());
            return pabellonRepo.save(pabellon);
        }
        return null;
    }

    // DELETE (logico — activo = 0)
    public boolean deletePabellon(Long id) {
        if (pabellonRepo.existsById(id)) {
            pabellonModel pabellon = pabellonRepo.findById(id).get();
            pabellon.setActivo(0);
            pabellonRepo.save(pabellon);
            return true;
        }
        return false;
    }


    // CAMAS

    // READ ALL por pabellon
    public List<camaModel> getCamasByPabellon(Long pabellonId) {
        return camaRepo.findByPabellonId(pabellonId);
    }

    // READ BY ID
    public Optional<camaModel> getCamaById(Long id) {
        return camaRepo.findById(id);
    }

    // CREATE
    public camaModel createCama(camaModel cama) {
        return camaRepo.save(cama);
    }

    // UPDATE (PUT)
    public camaModel updateCama(Long id, camaModel datos) {
        Optional<camaModel> existente = camaRepo.findById(id);

        if (existente.isPresent()) {
            camaModel cama = existente.get();
            cama.setNumeroCama(datos.getNumeroCama());
            cama.setEstado(datos.getEstado());
            cama.setPrioridadMin(datos.getPrioridadMin());
            return camaRepo.save(cama);
        }
        return null;
    }

    // PARTIAL UPDATE (PATCH) — actualiza solo el estado de la cama
    public Optional<camaModel> updateEstadoCama(Long id, String nuevoEstado) {
        Optional<camaModel> existente = camaRepo.findById(id);

        if (existente.isPresent()) {
            camaModel cama = existente.get();
            cama.setEstado(nuevoEstado);
            return Optional.of(camaRepo.save(cama));
        }
        return Optional.empty();
    }

    // DELETE BY ID
    public boolean deleteCama(Long id) {
        if (camaRepo.existsById(id)) {
            camaRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // DISPONIBILIDAD — responde a Api-clinica
    // Consulta disponibilidad por prioridad

    public camaDTO getDisponibilidad(int prioridad) {
        List<camaModel> disponibles = camaRepo.findDisponiblesPorPrioridad(prioridad);

        if (disponibles.isEmpty()) {
            return new camaDTO("N/A", prioridad, "Sin Disponibilidad", false);
        }

        camaModel primera = disponibles.get(0);
        return new camaDTO(
                String.valueOf(primera.getId()),
                prioridad,
                primera.getPabellon().getNombre(),
                true
        );
    }
}
