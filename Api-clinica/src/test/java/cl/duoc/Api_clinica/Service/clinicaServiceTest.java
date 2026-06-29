package cl.duoc.Api_clinica.Service;
import cl.duoc.Api_clinica.Model.clinicaModel;
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

public class clinicaServiceTest {
    @BeforeEach
    void setUp() {

        paciente = new ClinicaModel();

        paciente.setId(1L);
        paciente.setRut("12345678-9");
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setPrioridad(3);

    }

    @Test
    @DisplayName("Registrar paciente")
    void registrarPaciente(){
        when(repository.save(any(clinicaModel.class)))
                .thenReturn(paciente);
        clinicaModel resultado = service.registrar(paciente);
        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
        verify(repository).save(paciente);

    }

    @Test
    @DisplayName("Obtener todos los pacientes")
    void obtenerTodos(){
        when(repository.findAll())
                .thenReturn(Arrays.asList(paciente));
        List<clinicaModel> lista =
                service.obtenerTodos();
        assertEquals(1, lista.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Obtener pacientes críticos")
    void obtenerCriticos(){
        paciente.setPrioridad(1);
        when(repository.findByPrioridadLessThanEqual(2))
                .thenReturn(List.of(paciente));
        List<clinicaModel> lista =
                service.obtenerCriticos();
        assertEquals(1, lista.size());
        assertEquals(1,
                lista.get(0).getPrioridad());

    }

    @Test
    @DisplayName("Actualizar evolución")
    void actualizarEvolucion(){
        when(repository.findById(1))
                .thenReturn(Optional.of(paciente));
        when(repository.save(any()))
                .thenReturn(paciente);
        clinicaModel actualizado =
                service.actualizarEvolucion(
                        1,
                        "Paciente estable");
        assertNotNull(actualizado);
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Eliminar paciente")
    void eliminarPaciente(){
        when(repository.findById(1))
                .thenReturn(Optional.of(paciente));
        service.eliminar(1);
        verify(repository).deleteById(1);
    }

    @Test
    @DisplayName("No eliminar paciente prioridad 1")
    void noEliminarPacienteCritico(){
        paciente.setPrioridad(1);
        when(repository.findById(1))
                .thenReturn(Optional.of(paciente));
        assertThrows(RuntimeException.class,
                ()-> service.eliminar(1));
        verify(repository, never())
                .deleteById(anyInt());
    }

    @Test
    @DisplayName("Paciente inexistente")
    void pacienteNoExiste(){
        when(repository.findById(1))
                .thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                ()-> service.eliminar(1));
    }
}
