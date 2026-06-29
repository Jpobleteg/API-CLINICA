package cl.duoc.gestion_camas.Controller;

import cl.duoc.gestion_camas.Model.camaDTO;
import cl.duoc.gestion_camas.Model.camaModel;
import cl.duoc.gestion_camas.Model.pabellonModel;
import cl.duoc.gestion_camas.Service.camasService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/camas")
public class camasController {

    @Autowired
    camasService service;


    // PABELLONES


    // READ ALL
    @GetMapping("/pabellones")
    public ResponseEntity<List<pabellonModel>> obtenerTodosPabellones() {
        List<pabellonModel> pabellones = service.getAllPabellones();
        return new ResponseEntity<>(pabellones, HttpStatus.OK);
    }

    // READ BY ID
    @GetMapping("/pabellones/{id}")
    public ResponseEntity<pabellonModel> obtenerPabellonPorId(@PathVariable Long id) {
        return service.getPabellonById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping("/pabellones")
    public ResponseEntity<pabellonModel> crearPabellon(@Valid @RequestBody pabellonModel pabellon) {
        pabellonModel nuevo = service.createPabellon(pabellon);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    // UPDATE (PUT)
    @PutMapping("/pabellones/{id}")
    public ResponseEntity<pabellonModel> actualizarPabellon(
            @PathVariable Long id,
            @Valid @RequestBody pabellonModel datos) {

        pabellonModel actualizado = service.updatePabellon(id, datos);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE (logico)
    @DeleteMapping("/pabellones/{id}")
    public ResponseEntity<Void> desactivarPabellon(@PathVariable Long id) {
        if (service.deletePabellon(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


    // CAMAS


    // READ ALL por pabellon
    @GetMapping("/pabellon/{pabellonId}")
    public ResponseEntity<List<camaModel>> obtenerCamasPorPabellon(@PathVariable Long pabellonId) {
        List<camaModel> camas = service.getCamasByPabellon(pabellonId);
        return new ResponseEntity<>(camas, HttpStatus.OK);
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<camaModel> obtenerCamaPorId(@PathVariable Long id) {
        return service.getCamaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping
    public ResponseEntity<camaModel> crearCama(@Valid @RequestBody camaModel cama) {
        camaModel nueva = service.createCama(cama);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // UPDATE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<camaModel> actualizarCama(
            @PathVariable Long id,
            @Valid @RequestBody camaModel datos) {

        camaModel actualizada = service.updateCama(id, datos);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        }
        return ResponseEntity.notFound().build();
    }

    // PARTIAL UPDATE (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<camaModel> actualizarEstadoCama(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String nuevoEstado = request.get("estado");

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 si falta el campo "estado"
        }

        return service.updateEstadoCama(id, nuevoEstado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE BY ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCamaPorId(@PathVariable Long id) {
        if (service.deleteCama(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DISPONIBILIDAD — consumida por Api-clinica


    // GET api/v1/camas/disponibilidad/{prioridad}

    @GetMapping("/disponibilidad/{prioridad}")
    public ResponseEntity<camaDTO> consultarDisponibilidad(@PathVariable int prioridad) {
        return ResponseEntity.ok(service.getDisponibilidad(prioridad));
    }
}
