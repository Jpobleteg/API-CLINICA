package cl.duoc.Api_clinica.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "alertas_clinicas")
@Data
public class alertaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Ingrese la enfermedad.")
    private String enfermedadesPreexistentes;

    @Column(nullable = false)
    @NotBlank(message = "Ingrese las alergias.")
    private String alergias;

    @Column(nullable = false)
    @NotBlank(message = "Ingrese los medicamentos.")
    private String medicamentos;

    @Column(nullable = false)
    @NotBlank(message = "Ingrese observaciones.")
    private String observaciones;

}
