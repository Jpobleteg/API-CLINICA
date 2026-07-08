package cl.duoc.gestion_camas.Service;

import cl.duoc.gestion_camas.Model.CamaModel;
import cl.duoc.gestion_camas.Model.PabellonModel;
import cl.duoc.gestion_camas.Repository.CamaRepository;
import cl.duoc.gestion_camas.Repository.PabellonRepository;
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

@ExtendWith(MockitoExtension.class)
class CamasServiceTest { 

    @Mock
    private CamaRepository camaRepository;

    @Mock
    private PabellonRepository pabellonRepository;

    @InjectMocks
    private CamasService camasService; 

    private CamaModel cama;
    private PabellonModel pabellon;

    @BeforeEach
    void setUp() {
        // Inicializamos un pabellón válido y activo según las reglas de negocio
        pabellon = new PabellonModel();
        pabellon.setId(1L); // ID numérico Long
        pabellon.setNombre("Pabellón A");
        pabellon.setActivo(1);

        // Inicializamos la cama vinculada a dicho pabellón
        cama = new CamaModel();
        cama.setId(100L); // ID numérico Long
        cama.setNumeroCama("CAMA-001");
        cama.setEstado("DISPONIBLE");
        cama.setPrioridadMin(1);
        cama.setPabellon(pabellon);
    }

   
    // PRUEBAS
 

    @Test
    @DisplayName("getCamaById devuelve la cama cuando existe")
    void getCamaById_existe() {
        // Arrange
        when(camaRepository.findById(100L)).thenReturn(Optional.of(cama));

        // Act
        Optional<CamaModel> resultado = camasService.getCamaById(100L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("CAMA-001", resultado.get().getNumeroCama());
    }

    @Test
    @DisplayName("getCamasByPabellon lanza excepción si el pabellón no existe")
    void getCamasByPabellon_pabellonNoExiste() {
        // Arrange
        when(pabellonRepository.existsById(999L)).thenReturn(false);

        // Act & Assert (Verifica el manejo de errores exigido por la rúbrica)
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            camasService.getCamasByPabellon(999L);
        });

        assertTrue(excepcion.getMessage().contains("El pabellón con ID 999 no existe"));
        verify(camaRepository, never()).findByPabellonId(anyLong());
    }


    // PRUEBAS: CREAR

    @Test
    @DisplayName("createCama guarda exitosamente si el pabellón existe y está activo")
    void createCama_exitoso() {
        // Arrange
        when(pabellonRepository.findById(1L)).thenReturn(Optional.of(pabellon));
        when(camaRepository.save(any(CamaModel.class))).thenReturn(cama);

        // Act
        CamaModel resultado = camasService.createCama(cama);

        // Assert
        assertNotNull(resultado);
        assertEquals("CAMA-001", resultado.getNumeroCama());
        verify(camaRepository).save(cama);
    }

    @Test
    @DisplayName("createCama lanza excepción si el pabellón está inactivo")
    void createCama_pabellonInactivo() {
        // Arrange
        pabellon.setActivo(0); // Forzamos regla de negocio de inactividad
        when(pabellonRepository.findById(1L)).thenReturn(Optional.of(pabellon));

        // Act & Assert
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            camasService.createCama(cama);
        });

        assertEquals("No se puede asignar una cama a un Pabellón inactivo.", excepcion.getMessage());
        verify(camaRepository, never()).save(any(CamaModel.class));
    }

    // PRUEBAS: ACTUALIZAR Y VALIDAR

    @Test
    @DisplayName("updateEstadoCama lanza excepción ante un estado inválido")
    void updateEstadoCama_estadoInvalido() {
        // Act & Assert (Evalúa la regla de negocio añadida del catálogo controlado)
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            camasService.updateEstadoCama(100L, "ESTADO_FALSO");
        });

        assertTrue(excepcion.getMessage().contains("Valores permitidos: DISPONIBLE, OCUPADA, MANTENCION."));
        verify(camaRepository, never()).save(any(CamaModel.class));
    }

    @Test
    @DisplayName("updateCama lanza excepción si la cama a modificar no existe")
    void updateCama_noExiste() {
        // Arrange
        when(camaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            camasService.updateCama(999L, cama);
        });

        verify(camaRepository, never()).save(any(CamaModel.class));
    }

    // PRUEBAS: ELIMINAR

    @Test
    @DisplayName("deleteCama elimina y devuelve true si la cama existe")
    void deleteCama_existe() {
        // Arrange
        when(camaRepository.existsById(100L)).thenReturn(true);

        // Act
        boolean resultado = camasService.deleteCama(100L);

        // Assert
        assertTrue(resultado);
        verify(camaRepository).deleteById(100L);
    }
}
