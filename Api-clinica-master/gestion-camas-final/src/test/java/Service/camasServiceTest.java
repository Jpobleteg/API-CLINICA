package Service;

import cl.duoc.Api_clinica.Repository.camaRepository;
import cl.duoc.Api_clinica.Model.camaModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // 1. Activa Mockito
class CamaServiceTest {

    @Mock // 2. Repositorio falso (no toca la BD real)
    private camaRepository repository;

    @InjectMocks // 3. Service real con el mock inyectado automáticamente
    private camaService service;

    private camaModel cama;

    @BeforeEach // 4. Datos frescos antes de CADA test
    void setUp() {
        cama = new camaModel();
        cama.setId("CAMA-001");
        cama.setPrioridad(1);
        cama.setPabellon("Pabellon A");
        cama.setDisponible(true);
    }


    //  READ ALL


    @Test
    @DisplayName("getAllCamas devuelve la lista del repositorio")
    void getAllCamas_ok() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(cama));

        // Act
        List<camaModel> resultado = service.getAllCamas();

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("CAMA-001", resultado.get(0).getId());
        verify(repository).findAll();
    }


    //  READ BY ID


    @Test
    @DisplayName("getCamaById devuelve la cama cuando existe")
    void getCamaById_existe() {
        // Arrange
        when(repository.findById("CAMA-001")).thenReturn(Optional.of(cama));

        // Act
        Optional<camaModel> resultado = service.getCamaById("CAMA-001");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("CAMA-001", resultado.get().getId());
    }

    @Test
    @DisplayName("getCamaById devuelve vacío cuando NO existe")
    void getCamaById_noExiste() {
        // Arrange
        when(repository.findById("CAMA-999")).thenReturn(Optional.empty());

        // Act
        Optional<camaModel> resultado = service.getCamaById("CAMA-999");

        // Assert
        assertTrue(resultado.isEmpty());
    }


    //  READ BY PABELLON


    @Test
    @DisplayName("getCamasByPabellon devuelve camas del pabellón indicado")
    void getCamasByPabellon_ok() {
        // Arrange
        when(repository.findByPabellon("Pabellon A")).thenReturn(List.of(cama));

        // Act
        List<camaModel> resultado = service.getCamasByPabellon("Pabellon A");

        // Assert
        assertEquals(1, resultado.size());
        assertEquals("Pabellon A", resultado.get(0).getPabellon());
        verify(repository).findByPabellon("Pabellon A");
    }

    //  READ BY DISPONIBLE


    @Test
    @DisplayName("getCamasDisponibles devuelve solo camas disponibles")
    void getCamasDisponibles_ok() {
        // Arrange
        when(repository.findByDisponible(true)).thenReturn(List.of(cama));

        // Act
        List<camaModel> resultado = service.getCamasDisponibles(true);

        // Assert
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getDisponible());
        verify(repository).findByDisponible(true);
    }


    //  CREATE


    @Test
    @DisplayName("createCama guarda y devuelve la cama")
    void createCama_ok() {
        // Arrange
        when(repository.save(cama)).thenReturn(cama);

        // Act
        camaModel resultado = service.createCama(cama);

        // Assert
        assertNotNull(resultado);
        assertEquals("CAMA-001", resultado.getId());
        verify(repository).save(cama);
    }


    //  UPDATE (PUT)


    @Test
    @DisplayName("updateCama actualiza cuando la cama existe")
    void updateCama_existe() {
        // Arrange
        camaModel detalles = new camaModel();
        detalles.setPrioridad(2);
        detalles.setPabellon("Pabellon B");
        detalles.setDisponible(false);

        when(repository.findById("CAMA-001")).thenReturn(Optional.of(cama));
        when(repository.save(any(camaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        camaModel resultado = service.updateCama("CAMA-001", detalles);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getPrioridad());
        assertEquals("Pabellon B", resultado.getPabellon());
        assertFalse(resultado.getDisponible());
        verify(repository).save(cama);
    }

    @Test
    @DisplayName("updateCama devuelve null cuando la cama NO existe")
    void updateCama_noExiste() {
        // Arrange
        when(repository.findById("CAMA-999")).thenReturn(Optional.empty());

        // Act
        camaModel resultado = service.updateCama("CAMA-999", new camaModel());

        // Assert
        assertNull(resultado);
        verify(repository, never()).save(any()); // nunca debe guardar
    }


    //  PATCH — cambiar disponibilidad


    @Test
    @DisplayName("updateDisponibilidad cambia disponible a false cuando existe")
    void updateDisponibilidad_existe() {
        // Arrange
        when(repository.findById("CAMA-001")).thenReturn(Optional.of(cama));
        when(repository.save(any(camaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<camaModel> resultado = service.updateDisponibilidad("CAMA-001", false);

        // Assert
        assertTrue(resultado.isPresent());
        assertFalse(resultado.get().getDisponible());
    }

    @Test
    @DisplayName("updateDisponibilidad devuelve vacío cuando NO existe")
    void updateDisponibilidad_noExiste() {
        // Arrange
        when(repository.findById("CAMA-999")).thenReturn(Optional.empty());

        // Act
        Optional<camaModel> resultado = service.updateDisponibilidad("CAMA-999", false);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repository, never()).save(any());
    }

    
    //  DELETE


    @Test
    @DisplayName("deleteCama elimina y devuelve true cuando existe")
    void deleteCama_existe() {
        // Arrange
        when(repository.existsById("CAMA-001")).thenReturn(true);

        // Act
        boolean resultado = service.deleteCama("CAMA-001");

        // Assert
        assertTrue(resultado);
        verify(repository).deleteById("CAMA-001");
    }

    @Test
    @DisplayName("deleteCama devuelve false cuando NO existe")
    void deleteCama_noExiste() {
        // Arrange
        when(repository.existsById("CAMA-999")).thenReturn(false);

        // Act
        boolean resultado = service.deleteCama("CAMA-999");

        // Assert
        assertFalse(resultado);
        verify(repository, never()).deleteById(anyString());
    }
}
