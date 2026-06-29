package cl.duoc.Api_clinica.Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class alertaClinicaModelTest {
    @Test
    @DisplayName("Debe crear una alerta clínica")
    void crearAlerta(){
        Alerta_Clinica alerta = new AlertaClinica();
        alerta.setEnfermedadesPreexistentes("Diabetes");
        alerta.setAlergias("Penicilina");
        alerta.setMedicamentos("Metformina");
        alerta.setObservaciones("Paciente insulinodependiente");
        assertEquals("Diabetes",
                alerta.getEnfermedadesPreexistentes());
        assertEquals("Penicilina",
                alerta.getAlergias());
    }

    @Test
    @DisplayName("Debe modificar la enfermedad")
    void modificarEnfermedad(){
        Alerta_Clinica alerta = new AlertaClinica();
        alerta.setEnfermedadesPreexistentes("Hipertensión");
        assertEquals("Hipertensión",
                alerta.getEnfermedadesPreexistentes());
    }

    @Test
    @DisplayName("Debe modificar observaciones")
    void modificarObservacion(){
        Alerta_Clinica alerta = new AlertaClinica();
        alerta.setObservaciones("Paciente con marcapasos");
        assertEquals("Paciente con marcapasos",
                alerta.getObservaciones());
    }
}
