package cl.duoc.Api_clinica.Controller;

import cl.duoc.Api_clinica.Model.clinicaModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class clinicaControllerTest {
    @Test
    @DisplayName("Debe registrar un paciente")
    void registrarPaciente() throws Exception {
        clinicaModel paciente = crearPaciente();
        when(service.registrar(any(clinicaModel.class)))
                .thenReturn(paciente);
        mockMvc.perform(post("/api/clinica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.rut").value("12345678-9"));
    }


    @Test
    @DisplayName("Debe listar pacientes")
    void listarPacientes() throws Exception {
        when(service.obtenerTodos())
                .thenReturn(List.of(crearPaciente()));
        mockMvc.perform(get("/api/clinica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));
    }

    @Test
    @DisplayName("Debe listar pacientes críticos")
    void listarCriticos() throws Exception {
        clinicaModel paciente = crearPaciente();
        paciente.setPrioridad(1);
        when(service.obtenerCriticos())
                .thenReturn(List.of(paciente));
        mockMvc.perform(get("/api/clinica/criticos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].prioridad").value(1));
    }

    @Test
    @DisplayName("Debe actualizar la evolución")
    void actualizarEvolucion() throws Exception {
        clinicaModel paciente = crearPaciente();
        when(service.actualizarEvolucion(1L, "Paciente estable"))
                .thenReturn(paciente);
        mockMvc.perform(put("/api/clinica/1/evolucion")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Paciente estable"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Debe eliminar un paciente")
    void eliminarPaciente() throws Exception {
        doNothing().when(service).eliminar(1L);
        mockMvc.perform(delete("/api/clinica/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("No debe aceptar prioridad fuera del rango")
    void prioridadInvalida() throws Exception {
        clinicaModel paciente = crearPaciente();
        paciente.setPrioridad(8);
        mockMvc.perform(post("/api/clinica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("No debe aceptar nombre vacío")
    void nombreVacio() throws Exception {
        clinicaModel paciente = crearPaciente();
        paciente.setNombre("");
        mockMvc.perform(post("/api/clinica")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isBadRequest());
    }
}
