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

    @NotBlank(message = "El estado de prioridad es requerido (1-5)")
    private int prioridad;
}
