package cl.duoc.gestion_camas.Controller; 

import cl.duoc.gestion_camas.Model.CamaModel;
import cl.duoc.gestion_camas.Model.PabellonModel;
import cl.duoc.gestion_camas.Service.CamasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CamasControllerTest { 

    @Mock
    private CamasService camasService;

    @InjectMocks
    private CamasController camasController; // Controlador bajo prueba

    private CamaModel cama;
    private PabellonModel pabellon;

    @BeforeEach
    void setUp() {
        pabellon = new PabellonModel();
        pabellon.setId(1L);
        pabellon.setNombre("Pabellón A");
        pabellon.setActivo(1);

        cama = new CamaModel();
        cama.setId(100L); // IDs de tipo Long
        cama.setNumeroCama("CAMA-001");
        cama.setEstado("DISPONIBLE");
        cama.setPrioridadMin(1);
        cama.setPabellon(pabellon);
    }

    // TEST: BUSCAR POR ID

    @Test
    @DisplayName("obtenerCamaPorId responde 200 cuando existe la cama")
    void obtenerCamaPorId_existe() {
        // Arrange
        when(camasService.getCamaById(100L)).thenReturn(Optional.of(cama));

        // Act - Sincronizado con el método real del controlador
        ResponseEntity<CamaModel> resp = camasController.obtenerCamaPorId(100L);

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("CAMA-001", resp.getBody().getNumeroCama());
    }

    @Test
    @DisplayName("obtenerCamaPorId responde 404 cuando NO existe la cama")
    void obtenerCamaPorId_noExiste() {
        // Arrange
        when(camasService.getCamaById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<CamaModel> resp = camasController.obtenerCamaPorId(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }

    // TEST: POST

    @Test
    @DisplayName("crearCama responde 201 CREATED")
    void crearCama_ok() {
        // Arrange
        when(camasService.createCama(any(CamaModel.class))).thenReturn(cama);

        // Act
        ResponseEntity<CamaModel> resp = camasController.crearCama(cama);

        // Assert
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(100L, resp.getBody().getId());
    }

    // TEST: PATCH 

    @Test
    @DisplayName("actualizarEstadoCama responde 400 si falta el campo 'estado' en el Map")
    void actualizarEstadoCama_sinCampo() {
        // Act - Mandamos un mapa vacío
        ResponseEntity<CamaModel> resp = camasController.actualizarEstadoCama(100L, Map.of());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        verify(camasService, never()).updateEstadoCama(anyLong(), anyString());
    }

    @Test
    @DisplayName("actualizarEstadoCama responde 200 OK cuando se actualiza correctamente")
    void actualizarEstadoCama_ok() {
        // Arrange
        cama.setEstado("OCUPADA");
        when(camasService.updateEstadoCama(100L, "OCUPADA")).thenReturn(Optional.of(cama));

        // Act
        ResponseEntity<CamaModel> resp = camasController.actualizarEstadoCama(100L, Map.of("estado", "OCUPADA"));

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("OCUPADA", resp.getBody().getEstado());
    }

    // TEST: ELIMINAR
 
    @Test
    @DisplayName("eliminarCamaPorId responde 204 cuando se elimina con éxito")
    void eliminarCamaPorId_ok() {
        // Arrange
        when(camasService.deleteCama(100L)).thenReturn(true);

        // Act
        ResponseEntity<Void> resp = camasController.eliminarCamaPorId(100L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    @DisplayName("eliminarCamaPorId responde 404 si la cama a eliminar no existe")
    void eliminarCamaPorId_noExiste() {
        // Arrange
        when(camasService.deleteCama(999L)).thenReturn(false);

        // Act
        ResponseEntity<Void> resp = camasController.eliminarCamaPorId(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}
