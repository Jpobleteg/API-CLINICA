package Controller;

import cl.duoc.Api_clinica.Service.camaService;
import cl.duoc.Api_clinica.Model.camaModel;
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
class CamaControllerTest {

    @Mock
    private camaService service;

    @InjectMocks
    private camaController controller;

    private camaModel cama;

    @BeforeEach
    void setUp() {
        cama = new camaModel();
        cama.setId("CAMA-001");
        cama.setPrioridad(1);
        cama.setPabellon("Pabellon A");
        cama.setDisponible(true);
    }


    //  GET ALL


    @Test
    @DisplayName("obtenerTodas responde 200 con la lista")
    void obtenerTodas_ok() {
        // Arrange
        when(service.getAllCamas()).thenReturn(List.of(cama));

        // Act
        ResponseEntity<List<camaModel>> resp = controller.obtenerTodas();

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().size());
        assertEquals("CAMA-001", resp.getBody().get(0).getId());
    }


    //  GET BY ID


    @Test
    @DisplayName("obtenerPorId responde 200 cuando existe")
    void obtenerPorId_existe() {
        // Arrange
        when(service.getCamaById("CAMA-001")).thenReturn(Optional.of(cama));

        // Act
        ResponseEntity<camaModel> resp = controller.obtenerPorId("CAMA-001");

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("CAMA-001", resp.getBody().getId());
    }

    @Test
    @DisplayName("obtenerPorId responde 404 cuando NO existe")
    void obtenerPorId_noExiste() {
        // Arrange
        when(service.getCamaById("CAMA-999")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<camaModel> resp = controller.obtenerPorId("CAMA-999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }


    //  GET BY PABELLON


    @Test
    @DisplayName("obtenerPorPabellon responde 200 con camas del pabellón")
    void obtenerPorPabellon_ok() {
        // Arrange
        when(service.getCamasByPabellon("Pabellon A")).thenReturn(List.of(cama));

        // Act
        ResponseEntity<List<camaModel>> resp = controller.obtenerPorPabellon("Pabellon A");

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(1, resp.getBody().size());
    }

    //  CREATE


    @Test
    @DisplayName("crearCama responde 201 CREATED")
    void crearCama_ok() {
        // Arrange
        when(service.createCama(cama)).thenReturn(cama);

        // Act
        ResponseEntity<camaModel> resp = controller.crearCama(cama);

        // Assert
        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals("CAMA-001", resp.getBody().getId());
    }


    //  PATCH — actualizar disponibilidad


    @Test
    @DisplayName("actualizarDisponibilidad responde 400 si falta el campo disponible")
    void actualizarDisponibilidad_sinCampo() {
        // Act — se manda un body vacío sin el campo "disponible"
        ResponseEntity<camaModel> resp =
                controller.actualizarDisponibilidad("CAMA-001", Map.of());

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        verify(service, never()).updateDisponibilidad(anyString(), anyBoolean());
    }

    @Test
    @DisplayName("actualizarDisponibilidad responde 200 cuando actualiza")
    void actualizarDisponibilidad_ok() {
        // Arrange
        cama.setDisponible(false);
        when(service.updateDisponibilidad("CAMA-001", false))
                .thenReturn(Optional.of(cama));

        // Act
        ResponseEntity<camaModel> resp =
                controller.actualizarDisponibilidad("CAMA-001", Map.of("disponible", false));

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertFalse(resp.getBody().getDisponible());
    }

    @Test
    @DisplayName("actualizarDisponibilidad responde 404 cuando la cama NO existe")
    void actualizarDisponibilidad_noExiste() {
        // Arrange
        when(service.updateDisponibilidad("CAMA-999", true)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<camaModel> resp =
                controller.actualizarDisponibilidad("CAMA-999", Map.of("disponible", true));

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }


    //  UPDATE (PUT)


    @Test
    @DisplayName("actualizarCama responde 200 cuando existe")
    void actualizarCama_ok() {
        // Arrange
        when(service.updateCama("CAMA-001", cama)).thenReturn(cama);

        // Act
        ResponseEntity<camaModel> resp = controller.actualizarCama("CAMA-001", cama);

        // Assert
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("CAMA-001", resp.getBody().getId());
    }

    @Test
    @DisplayName("actualizarCama responde 404 cuando NO existe")
    void actualizarCama_noExiste() {
        // Arrange
        when(service.updateCama("CAMA-999", cama)).thenReturn(null);

        // Act
        ResponseEntity<camaModel> resp = controller.actualizarCama("CAMA-999", cama);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }


    //  DELETE


    @Test
    @DisplayName("eliminarCama responde 204 cuando elimina")
    void eliminarCama_ok() {
        // Arrange
        when(service.deleteCama("CAMA-001")).thenReturn(true);

        // Act
        ResponseEntity<Void> resp = controller.eliminarCama("CAMA-001");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
    }

    @Test
    @DisplayName("eliminarCama responde 404 cuando NO existe")
    void eliminarCama_noExiste() {
        // Arrange
        when(service.deleteCama("CAMA-999")).thenReturn(false);

        // Act
        ResponseEntity<Void> resp = controller.eliminarCama("CAMA-999");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
    }
}
