package cl.duoc.Api_clinica.Controller;

import cl.duoc.Api_clinica.Service.clinicaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinica")

public class clinicaController {
    @Autowired
    private clinicaService service;

    @PostMapping
    public ResponseEntity<Paciente>
    registrar(@Valid @RequestBody Paciente paciente){

        return ResponseEntity.ok(
                service.registrar(paciente));
    }

    @GetMapping
    public List<Paciente> listar(){
        return service.obtenerTodos();
    }

    @GetMapping("/criticos")
    public List<Paciente> criticos(){
        return service.obtenerCriticos();
    }

    @PutMapping("/{id}/evolucion")
    public Paciente actualizar(
            @PathVariable Long id,
            @RequestBody String evolucion){

        return service.actualizarEvolucion(
                id,
                evolucion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id){

        service.eliminar(id);

        return ResponseEntity.ok().build();
    }
}