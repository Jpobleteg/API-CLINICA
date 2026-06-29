package cl.duoc.gestion_camas.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "PABELLONES")
@Data
public class pabellonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "NOMBRE")
    @NotBlank(message = "El nombre del pabellon es obligatorio y no puede estar en blanco.")
    private String nombre;

    @Column(nullable = false, name = "DESCRIPCION")
    private String descripcion;

    @Column(nullable = false, name = "CAPACIDAD")
    private int capacidad;

    @Column(nullable = false, name = "ACTIVO")
    private int activo = 1;

    @OneToMany(mappedBy = "pabellon", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<camaModel> camas;

}
