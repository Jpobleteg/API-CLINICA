package cl.duoc.Api_clinica.Controller;

import cl.duoc.Api_clinica.Model.PacienteModel;
import cl.duoc.Api_clinica.Service.ClinicaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List; 

@RestController
@RequestMapping("/api/clinica")
public class ClinicaController { 

    private final ClinicaService clinicaService;

    public ClinicaController(ClinicaService clinicaService) {
        this.clinicaService = clinicaService;
    }

    @PostMapping
    public ResponseEntity<PacienteModel> registrar(@Valid @RequestBody PacienteModel paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clinicaService.registrar(paciente));
    }

    @GetMapping
    public ResponseEntity<List<PacienteModel>> listar() {
        return ResponseEntity.ok(clinicaService.obtenerTodos());
    }

    @GetMapping("/criticos")
    public ResponseEntity<List<PacienteModel>> criticos() {
        return ResponseEntity.ok(clinicaService.obtenerCriticos());
    }

    @PutMapping("/{id}/evolucion")
    public ResponseEntity<PacienteModel> actualizar(@PathVariable Long id, @RequestBody String evolucion) {
        return ResponseEntity.ok(clinicaService.actualizarEvolucion(id, evolucion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clinicaService.eliminar(id);
        return ResponseEntity.noContent().build(); 
    }
}
