package cl.duoc.Api_clinica.Service; 

import cl.duoc.Api_clinica.Model.ClinicaModel;
import cl.duoc.Api_clinica.Repository.ClinicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections; 
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
public class ClinicaServiceTest { 

    @Mock 
    private ClinicaRepository repository;

    @InjectMocks 
    private ClinicaService service;

    private ClinicaModel paciente;

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
        when(repository.save(any(ClinicaModel.class))).thenReturn(paciente); 
        
        ClinicaModel resultado = service.registrar(paciente);
        assertNotNull(resultado); 
        assertEquals("Juan", resultado.getNombre()); 
        verify(repository).save(paciente); 
    }

    @Test
    @DisplayName("Obtener todos los pacientes")
    void obtenerTodos(){
        when(repository.findAll()).thenReturn(Collections.singletonList(paciente));
        
        List<ClinicaModel> lista = service.obtenerTodos();
        assertEquals(1, lista.size()); 
        verify(repository).findAll(); 
    }

    @Test
    @DisplayName("Obtener pacientes críticos")
    void obtenerCriticos(){
        paciente.setPrioridad(1);
        when(repository.findByPrioridadLessThanEqual(2)).thenReturn(List.of(paciente));
        
        List<ClinicaModel> lista = service.obtenerCriticos(); 
        assertEquals(1, lista.size()); 
        assertEquals(1, lista.get(0).getPrioridad()); 
    }

    @Test
    @DisplayName("Actualizar evolución")
    void actualizarEvolucion(){
        when(repository.findById(1L)).thenReturn(Optional.of(paciente));
        when(repository.save(any(ClinicaModel.class))).thenReturn(paciente); 
        
        ClinicaModel actualizado = service.actualizarEvolucion(1L, "Paciente estable");
        assertNotNull(actualizado); 
        verify(repository).save(any(ClinicaModel.class)); 
    }

    @Test
    @DisplayName("Eliminar paciente")
    void eliminarPaciente(){
        when(repository.findById(1L)).thenReturn(Optional.of(paciente)); 
        service.eliminar(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("No eliminar paciente prioridad 1")
    void noEliminarPacienteCritico(){
        paciente.setPrioridad(1);
        when(repository.findById(1L)).thenReturn(Optional.of(paciente)); 
        
        assertThrows(RuntimeException.class, () -> service.eliminar(1L));
        verify(repository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Paciente inexistente")
    void pacienteNoExiste(){
        when(repository.findById(1L)).thenReturn(Optional.empty()); 
        assertThrows(RuntimeException.class, () -> service.eliminar(1L));
    }
}
