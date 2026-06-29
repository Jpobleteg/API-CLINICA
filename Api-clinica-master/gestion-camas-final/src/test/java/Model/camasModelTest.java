package Model;

package cl.duoc.Api_clinica.Model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CamaModelValidationTest {

    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    //  CASO VÁLIDO

    @Test
    @DisplayName("Una cama válida no genera violaciones")
    void camaValida() {
        // Arrange
        camaModel c = new camaModel();
        c.setId("CAMA-001");
        c.setPrioridad(1);
        c.setPabellon("Pabellon A");
        c.setDisponible(true);

        // Act
        Set<ConstraintViolation<camaModel>> violaciones = validator.validate(c);

        // Assert
        assertTrue(violaciones.isEmpty());
    }


    //  VALIDACIÓN DEL ID


    @Test
    @DisplayName("id en blanco genera violación (@NotBlank)")
    void idEnBlanco() {
        // Arrange
        camaModel c = new camaModel();
        c.setId("");          // inválido — no puede estar vacío
        c.setPrioridad(1);
        c.setPabellon("Pabellon A");
        c.setDisponible(true);

        // Act
        Set<ConstraintViolation<camaModel>> violaciones = validator.validate(c);

        // Assert
        assertFalse(violaciones.isEmpty());
    }

    @Test
    @DisplayName("id nulo genera violación (@NotNull / @NotBlank)")
    void idNulo() {
        // Arrange
        camaModel c = new camaModel();
        c.setId(null);
        c.setPrioridad(1);
        c.setPabellon("Pabellon A");
        c.setDisponible(true);

        // Act
        Set<ConstraintViolation<camaModel>> violaciones = validator.validate(c);

        // Assert
        assertFalse(violaciones.isEmpty());
    }


    //  VALIDACIÓN DE PRIORIDAD


    @Test
    @DisplayName("prioridad 0 genera violación (@Min)")
    void prioridadCero() {
        // Arrange
        camaModel c = new camaModel();
        c.setId("CAMA-001");
        c.setPrioridad(0);    // inválido — mínimo es 1
        c.setPabellon("Pabellon A");
        c.setDisponible(true);

        // Act
        Set<ConstraintViolation<camaModel>> violaciones = validator.validate(c);

        // Assert
        assertFalse(violaciones.isEmpty());
    }

    @Test
    @DisplayName("Las prioridades válidas (1 a 5) no generan violaciones")
    void prioridadesValidas() {
        for (int p = 1; p <= 5; p++) {
            camaModel c = new camaModel();
            c.setId("CAMA-00" + p);
            c.setPrioridad(p);
            c.setPabellon("Pabellon A");
            c.setDisponible(true);

            assertTrue(
                    validator.validate(c).isEmpty(),
                    "Prioridad " + p + " debería ser válida"
            );
        }
    }


    //  VALIDACIÓN DE PABELLON


    @Test
    @DisplayName("pabellon en blanco genera violación (@NotBlank)")
    void pabellonEnBlanco() {
        // Arrange
        camaModel c = new camaModel();
        c.setId("CAMA-001");
        c.setPrioridad(1);
        c.setPabellon("");    // inválido
        c.setDisponible(true);

        // Act
        Set<ConstraintViolation<camaModel>> violaciones = validator.validate(c);

        // Assert
        assertFalse(violaciones.isEmpty());
    }


    //  VALIDACIÓN DE DISPONIBLE


    @Test
    @DisplayName("disponible nulo genera violación (@NotNull)")
    void disponibleNulo() {
        // Arrange
        camaModel c = new camaModel();
        c.setId("CAMA-001");
        c.setPrioridad(1);
        c.setPabellon("Pabellon A");
        c.setDisponible(null); // inválido

        // Act
        Set<ConstraintViolation<camaModel>> violaciones = validator.validate(c);

        // Assert
        assertFalse(violaciones.isEmpty());
    }

    @Test
    @DisplayName("disponible true y false son ambos valores válidos")
    void disponibleAmbosValores() {
        for (Boolean valor : new Boolean[]{true, false}) {
            camaModel c = new camaModel();
            c.setId("CAMA-001");
            c.setPrioridad(1);
            c.setPabellon("Pabellon A");
            c.setDisponible(valor);

            assertTrue(
                    validator.validate(c).isEmpty(),
                    "disponible=" + valor + " debería ser válido"
            );
        }
    }
}
