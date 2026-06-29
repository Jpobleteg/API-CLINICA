package cl.duoc.Api_clinica.Service;

import cl.duoc.Api_clinica.Model.clinicaModel;
import cl.duoc.Api_clinica.Repository.clinicaRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
@Service
public class clinicaService {


    @Autowired
    private clinicaRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final String CAMAS_API_URL

    @Transactional
    public clinicaModel registrarPaciente(@NonNull clinicaModel nuevoPaciente{

        CamaDTO cama;

        String urlDestino =
                CAMAS_API_URL + nuevoPaciente.getPrioridad();

        try {

            cama = restTemplate.getForObject(
                    urlDestino,
                    CamaDTO.class
            );

        } catch (HttpClientErrorException.NotFound e) {

            nuevoPaciente.setEstado(
                    "LISTA_ESPERA_CRITICA"
            );

            nuevoPaciente.setFechaIngreso(
                    LocalDateTime.now()
            );

            return repository.save(nuevoPaciente);

        } catch (Exception e) {

            nuevoPaciente.setEstado(
                    "LISTA_ESPERA_CRITICA"
            );

            nuevoPaciente.setFechaIngreso(
                    LocalDateTime.now()
            );

            return repository.save(nuevoPaciente);
        }

        if (cama == null ||
                cama.getDisponible() == null ||
                !cama.getDisponible()) {

            nuevoPaciente.setEstado(
                    "LISTA_ESPERA_CRITICA"
            );

            nuevoPaciente.setFechaIngreso(
                    LocalDateTime.now()
            );

            return repository.save(nuevoPaciente);
        }

        // RESERVA DE CAMA

        try {

            CamaDTO patchBody = new CamaDTO();

            patchBody.setDisponible(false);

            HttpEntity<CamaDTO> requestEntity =
                    new HttpEntity<>(patchBody);

            restTemplate.exchange(
                    urlDestino,
                    HttpMethod.PATCH,
                    requestEntity,
                    Void.class
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al actualizar la disponibilidad de la cama."
            );
        }

        nuevoPaciente.setEstado("EN_ESPERA");

        nuevoPaciente.setFechaIngreso(
                LocalDateTime.now()
        );

        return repository.save(nuevoPaciente);
}
