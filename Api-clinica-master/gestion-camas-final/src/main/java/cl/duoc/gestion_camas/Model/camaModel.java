package cl.duoc.gestion_camas.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "CAMAS")
@Data
public class camaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PABELLON_ID", nullable = false)
    private pabellonModel pabellon;

    @Column(nullable = false, name = "NUMERO_CAMA")
    @NotBlank(message = "El numero de cama es obligatorio y no puede estar en blanco.")
    private String numeroCama;

    @Column(nullable = false, name = "ESTADO")
    @NotBlank(message = "El estado es requerido.")
    @Pattern(regexp = "DISPONIBLE|OCUPADA|EN_MANTENIMIENTO",
             message = "El estado debe ser DISPONIBLE, OCUPADA o EN_MANTENIMIENTO.")
    private String estado;

    @Column(nullable = false, name = "PRIORIDAD_MIN")
    private int prioridadMin;

}
