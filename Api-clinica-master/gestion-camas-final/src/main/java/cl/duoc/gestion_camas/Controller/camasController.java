package cl.duoc.gestion_camas.Controller;

import cl.duoc.gestion_camas.Model.CamaDTO; // Asumiendo corrección de nombre a Mayúscula
import cl.duoc.gestion_camas.Model.CamaModel;
import cl.duoc.gestion_camas.Model.PabellonModel;
import cl.duoc.gestion_camas.Service.CamasService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/camas") 
public class CamasController { 

    private final CamasService camasService;

    public CamasController(CamasService camasService) {
        this.camasService = camasService;
    }

    // SECCIÓN PABELLONES
    
    @GetMapping("/pabellones")
    public ResponseEntity<List<PabellonModel>> obtenerTodosPabellones() {
        List<PabellonModel> pabellones = camasService.getAllPabellones();
        return ResponseEntity.ok(pabellones);
    }

    @GetMapping("/pabellones/{id}")
    public ResponseEntity<PabellonModel> obtenerPabellonPorId(@PathVariable Long id) {
        return camasService.getPabellonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
                // NOTA: Lo ideal será reemplazar este .orElse por excepciones del Service
    }

    @PostMapping("/pabellones")
    public ResponseEntity<PabellonModel> crearPabellon(@Valid @RequestBody PabellonModel pabellon) {
        PabellonModel nuevo = camasService.createPabellon(pabellon);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/pabellones/{id}")
    public ResponseEntity<PabellonModel> actualizarPabellon(
            @PathVariable Long id,
            @Valid @RequestBody PabellonModel datos) {

        PabellonModel actualizado = camasService.updatePabellon(id, datos);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/pabellones/{id}")
    public ResponseEntity<Void> desactivarPabellon(@PathVariable Long id) {
        if (camasService.deletePabellon(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // SECCIÓN CAMAS

    @GetMapping("/pabellon/{pabellonId}")
    public ResponseEntity<List<CamaModel>> obtenerCamasPorPabellon(@PathVariable Long pabellonId) {
        List<CamaModel> camas = camasService.getCamasByPabellon(pabellonId);
        return ResponseEntity.ok(camas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamaModel> obtenerCamaPorId(@PathVariable Long id) {
        return camasService.getCamaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CamaModel> crearCama(@Valid @RequestBody CamaModel cama) {
        CamaModel nueva = camasService.createCama(cama);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamaModel> actualizarCama(
            @PathVariable Long id,
            @Valid @RequestBody CamaModel datos) {

        CamaModel actualizada = camasService.updateCama(id, datos);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CamaModel> actualizarEstadoCama(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String nuevoEstado = request.get("estado");

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); 
        }

        return camasService.updateEstadoCama(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCamaPorId(@PathVariable Long id) {
        if (camasService.deleteCama(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // DISPONIBILIDAD (Comunicación entre Microservicios)

    @GetMapping("/disponibilidad/{prioridad}")
    public ResponseEntity<CamaDTO> consultarDisponibilidad(@PathVariable int prioridad) {
        return ResponseEntity.ok(camasService.getDisponibilidad(prioridad));
    }
}
