package cl.duoc.Api_clinica.Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class clinicaModelTest {
    @Test
    @DisplayName("Debe crear un paciente correctamente")
    void crearPaciente() {
        clinicaModel paciente = new clinicaModel();
        paciente.setId(1);
        paciente.setRut("12345678-9");
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setPrioridad(3);
        assertEquals(1, paciente.getId());
        assertEquals("12345678-9", paciente.getRut());
        assertEquals("Juan", paciente.getNombre());
        assertEquals("Pérez", paciente.getApellido());
        assertEquals(3, paciente.getPrioridad());
    }

    @Test
    @DisplayName("Debe modificar la prioridad")
    void modificarPrioridad(){
        clinicaModel paciente = new clinicaModel();
        paciente.setPrioridad(2);
        assertEquals(2, paciente.getPrioridad());
    }

    @Test
    @DisplayName("Debe modificar el nombre")
    void modificarNombre(){
        clinicaModel paciente = new clinicaModel();
        paciente.setNombre("María");
        assertEquals("María", paciente.getNombre());
    }

    @Test
    @DisplayName("Debe modificar el apellido")
    void modificarApellido(){
        clinicaModel paciente = new clinicaModel();
        paciente.setApellido("Soto");
        assertEquals("Soto", paciente.getApellido());
    }

    @Test
    @DisplayName("Debe modificar el RUT")
    void modificarRut(){
        clinicaModel paciente = new clinicaModel();
        paciente.setRut("11111111-1");
        assertEquals("11111111-1", paciente.getRut());
    }

    @Test
    @DisplayName("Paciente crítico")
    void pacienteCritico(){
        clinicaModel paciente = new clinicaModel();
        paciente.setPrioridad(1);
        assertTrue(paciente.getPrioridad() <= 2);
    }

    @Test
    @DisplayName("Paciente no crítico")
    void pacienteNoCritico(){
        clinicaModel paciente = new clinicaModel();
        paciente.setPrioridad(5);
        assertFalse(paciente.getPrioridad() <= 2);
    }

    
}
