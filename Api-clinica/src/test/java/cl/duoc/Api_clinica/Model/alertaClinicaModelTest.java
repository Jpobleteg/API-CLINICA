package cl.duoc.Api_clinica.Model; 

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlertaClinicaModelTest { 

    @Test
    @DisplayName("Debe crear una alerta clínica")
    void crearAlerta(){
        AlertaClinicaModel alerta = new AlertaClinicaModel(); 
        alerta.setEnfermedadesPreexistentes("Diabetes"); 
        alerta.setAlergias("Penicilina"); 
        alerta.setMedicamentos("Metformina"); 
        alerta.setObservaciones("Paciente insulinodependiente"); 
        assertEquals("Diabetes", alerta.getEnfermedadesPreexistentes()); 
        assertEquals("Penicilina", alerta.getAlergias()); 
    }

    @Test
    @DisplayName("Debe modificar la enfermedad")
    void modificarEnfermedad(){
        AlertaClinicaModel alerta = new AlertaClinicaModel();
        alerta.setEnfermedadesPreexistentes("Hipertensión");
        assertEquals("Hipertensión", alerta.getEnfermedadesPreexistentes()); 
    }

    @Test
    @DisplayName("Debe modificar observaciones")
    void modificarObservacion(){
        AlertaClinicaModel alerta = new AlertaClinicaModel(); 
        alerta.setObservaciones("Paciente con marcapasos"); 
        assertEquals("Paciente con marcapasos", alerta.getObservaciones()); 
    }
}
