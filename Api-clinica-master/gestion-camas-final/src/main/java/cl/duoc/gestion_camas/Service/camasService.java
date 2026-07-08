package cl.duoc.gestion_camas.Service;

import cl.duoc.gestion_camas.Model.CamaDTO;
import cl.duoc.gestion_camas.Model.CamaModel;
import cl.duoc.gestion_camas.Model.PabellonModel;
import cl.duoc.gestion_camas.Repository.CamaRepository;
import cl.duoc.gestion_camas.Repository.PabellonRepository;
import cl.duoc.gestion_camas.Exception.ResourceNotFoundException; 

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CamasService { 

    private final PabellonRepository pabellonRepo;
    private final CamaRepository camaRepo;

    public CamasService(PabellonRepository pabellonRepo, CamaRepository camaRepo) {
        this.pabellonRepo = pabellonRepo;
        this.camaRepo = camaRepo;
    }

    // SECCIÓN PABELLONES

    @Transactional(readOnly = true)
    public List<PabellonModel> getAllPabellones() {
        return pabellonRepo.findByActivo(1);
    }

    @Transactional(readOnly = true)
    public Optional<PabellonModel> getPabellonById(Long id) {
        return pabellonRepo.findById(id);
    }

    @Transactional
    public PabellonModel createPabellon(PabellonModel pabellon) {
        pabellon.setActivo(1); // Regla de Negocio: Todo pabellón nuevo inicia activo
        return pabellonRepo.save(pabellon);
    }

    @Transactional
    public PabellonModel updatePabellon(Long id, PabellonModel datos) {
        // En lugar de retornar null, se procesa funcionalmente u orElseThrow
        return pabellonRepo.findById(id)
                .map(pabellon -> {
                    pabellon.setNombre(datos.getNombre());
                    pabellon.setDescripcion(datos.getDescripcion());
                    pabellon.setCapacidad(datos.getCapacidad());
                    return pabellonRepo.save(pabellon);
                })
                .orElseThrow(() -> new RuntimeException("Pabellón no encontrado con el ID: " + id));
                // Consejo: Reemplazar RuntimeException por una excepción de negocio propia
    }

    @Transactional
    public boolean deletePabellon(Long id) {
        return pabellonRepo.findById(id)
                .map(pabellon -> {
                    pabellon.setActivo(0); // Borrado lógico
                    pabellonRepo.save(pabellon);
                    return true;
                })
                .orElse(false);
    }

    // SECCIÓN CAMAS

    @Transactional(readOnly = true)
    public List<CamaModel> getCamasByPabellon(Long pabellonId) {
        // Regla de Negocio: Validar primero si el pabellón existe
        if (!pabellonRepo.existsById(pabellonId)) {
            throw new RuntimeException("No se pueden listar camas. El pabellón con ID " + pabellonId + " no existe.");
        }
        return camaRepo.findByPabellonId(pabellonId);
    }

    @Transactional(readOnly = true)
    public Optional<CamaModel> getCamaById(Long id) {
        return camaRepo.findById(id);
    }

    @Transactional
    public CamaModel createCama(CamaModel cama) {
        // Regla de Negocio Crítica: Validar que el pabellón asociado exista y esté activo
        if (cama.getPabellon() == null || cama.getPabellon().getId() == null) {
            throw new RuntimeException("La cama debe estar asociada a un Pabellón válido.");
        }
        
        PabellonModel pabellon = pabellonRepo.findById(cama.getPabellon().getId())
                .orElseThrow(() -> new RuntimeException("El Pabellón especificado no existe."));
                
        if (pabellon.getActivo() != 1) {
            throw new RuntimeException("No se puede asignar una cama a un Pabellón inactivo.");
        }

        return camaRepo.save(cama);
    }

    @Transactional
    public CamaModel updateCama(Long id, CamaModel datos) {
        return camaRepo.findById(id)
                .map(cama -> {
                    cama.setNumeroCama(datos.getNumeroCama());
                    
                    // Validación de Regla de Negocio para el estado
                    validarEstadoCama(datos.getEstado());
                    cama.setEstado(datos.getEstado());
                    
                    cama.setPrioridadMin(datos.getPrioridadMin());
                    return camaRepo.save(cama);
                })
                .orElseThrow(() -> new RuntimeException("Cama no encontrada con el ID: " + id));
    }

    @Transactional
    public Optional<CamaModel> updateEstadoCama(Long id, String nuevoEstado) {
        validarEstadoCama(nuevoEstado); // Validación de negocio antes de persistir
        
        return camaRepo.findById(id)
                .map(cama -> {
                    cama.setEstado(nuevoEstado);
                    return camaRepo.save(cama);
                });
    }

    @Transactional
    public boolean deleteCama(Long id) {
        if (camaRepo.existsById(id)) {
            camaRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // DISPONIBILIDAD (Comunicación Microservicios)

    @Transactional(readOnly = true)
    public CamaDTO getDisponibilidad(int prioridad) {
        List<CamaModel> disponibles = camaRepo.findDisponiblesPorPrioridad(prioridad);

        if (disponibles.isEmpty()) {
            return new CamaDTO("N/A", prioridad, "Sin Disponibilidad", false);
        }

        // Se obtiene la primera cama que cumple el criterio de disponibilidad
        CamaModel primera = disponibles.get(0);
        
        // Evitar potenciales NullPointerException si el mapeo de la relación falla
        String nombrePabellon = (primera.getPabellon() != null) ? primera.getPabellon().getNombre() : "Pabellón Desconocido";

        return new CamaDTO(
                String.valueOf(primera.getId()),
                prioridad,
                nombrePabellon,
                true
        );
    }

    // UTILERÍA / VALIDACIÓN DE NEGOCIO
    private void validarEstadoCama(String estado) {
        if (estado == null) {
            throw new RuntimeException("El estado de la cama no puede ser nulo.");
        }
        String estadoUpper = estado.trim().toUpperCase();
        if (!estadoUpper.equals("DISPONIBLE") && !estadoUpper.equals("OCUPADA") && !estadoUpper.equals("MANTENCION")) {
            throw new RuntimeException("Estado '" + estado + "' no es válido. Valores permitidos: DISPONIBLE, OCUPADA, MANTENCION.");
        }
    }
}
