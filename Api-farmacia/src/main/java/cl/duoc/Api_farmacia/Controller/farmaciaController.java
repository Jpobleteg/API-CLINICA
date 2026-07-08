package cl.duoc.Api_farmacia.Controller;

import cl.duoc.pharmacy_service.Model.RecetaInsumoModel;
import cl.duoc.pharmacy_service.Service.PharmacyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/farmacia")
@Tag(name = "Farmacia e Insumos Críticos", description = "Endpoints para la gestión automática de recetas y kits médicos de emergencia")
public class farmaciaController {

        private final farmaciaService FarmaciaService;

        public PharmacyController(FarmaciaService farmaciaService) {
            this.farmaciaService = farmaciaService;
        }

        @PostMapping("/solicitar-kit")
        @Operation(summary = "Solicitar Kit de Insumos Médicos de Urgencia", description = "Crea una orden de despacho de medicamentos enlazada a un paciente y su cama asignada.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "201", description = "Orden de insumos registrada y en preparación exitosamente"),
                @ApiResponse(responseCode = "400", description = "Petición corrupta o faltan identificadores obligatorios")
        })
        public ResponseEntity<FarmaciaModel> prepararKitUrgencia(@RequestBody FarmaciaModel solicitud) {
            FarmaciaModel nuevoPedido = farmaciaService.procesarPedido(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
        }

        @GetMapping("/pedidos")
        @Operation(summary = "Listar todas las órdenes de insumos")
        public ResponseEntity<List<FarmaciaModel>> listarTodos() {
            return ResponseEntity.ok(farmaciaService.obtainTodosLosPedidos());
        }

        @PatchMapping("/pedidos/{id}/estado")
        @Operation(summary = "Actualizar el estado de despacho de una receta")
        public ResponseEntity<FarmaciaModel> cambiarEstado(
                @Parameter(description = "ID de la orden de insumos", example = "1") @PathVariable Long id,
                @RequestBody Map<String, String> body) {

            String nuevoEstado = body.get("estado");
            if (nuevoEstado == null) {
                return ResponseEntity.badRequest().build();
            }

            return farmaciaService.actualizarEstadoPedido(id, nuevoEstado)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
    }
}
