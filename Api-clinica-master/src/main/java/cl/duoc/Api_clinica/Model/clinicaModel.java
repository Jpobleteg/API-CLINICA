package cl.duoc.Api_clinica.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
@Table(name="pacientes")

public class clinicaModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, name="rut")
    @NotBlank(message = "Ingresar rut en formato xxxxxxxx-x")
    private String rut;

    @Column(nullable = false, name="nombre")
    @NotBlank(message = "Ingresar solo primer nombre")
    private String nombre;

    @Column(nullable = false, name="apellido")
    @NotBlank(message = "Ingresar solo primer apellido")
    private String apellido;

    @Min(value = 1, message = "La prioridad mínima es 1.")
    @Max(value = 5, message = "La prioridad máxima es 5.")
    private Integer prioridad;
}
