package cl.duoc.Api_farmacia.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

    @Entity
    @Table(name = "RECETAS_INSUMOS")

public class farmaciaModel {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "paciente_id", nullable = false)
        private Long pacienteId;// ID vinculado al microservicio de Triage Clínico

        @Column(name = "cama_id", nullable = false)
        private Long camaId;// ID vinculado al microservicio de Gestión de Camas

        @Column(name = "descripcion_kit", nullable = false)
        private String descripcionKit;

        @Column(nullable = false)
        private String estado;

        @Column(name = "fecha_solicitud", nullable = false)
        private LocalDateTime fechaSolicitud;
}
