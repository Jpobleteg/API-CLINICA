package cl.duoc.Api_clinica.Service;

import cl.duoc.Api_clinica.Repository.clinicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class clinicaService {

    @Autowired
    clinicaRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    private final String SCOOTER_API_URL = "http://localhost:25001/api/v1/clinica/";

    public Paciente registrar(Paciente paciente){
        try {
            boolean disponible =
                    camaClient.verificarDisponibilidad(
                            paciente.getPrioridad());
            if(!disponible){
                throw new SinDisponibilidadException(
                        "No existen camas disponibles");
            }
            paciente.setEstado("INGRESADO");
        } catch (SinDisponibilidadException e){
            paciente.setEstado("LISTA_ESPERA_CRITICA");
        } catch (Exception e){
            paciente.setEstado("Error, vuelva a intentar la consulta");
        }
        return repository.save(paciente);
    }

    public List<Paciente> obtenerTodos(){
        return repository.findAll();
    }

    public List<Paciente> obtenerCriticos(){
        return repository.findByPrioridadLessThanEqual(2);
    }
    public Paciente actualizarEvolucion(
            Long id,
            String evolucion){
        Paciente paciente = repository.findById(id).orElseThrow();
        paciente.setEvolucionClinica(evolucion);
        return repository.save(paciente);
    }

    public void eliminar(Long id){
        Paciente paciente = repository.findById(id).orElseThrow();
        if(paciente.getPrioridad() <= 2){
            throw new RuntimeException("No se puede eliminar un paciente crítico");
        }
        repository.deleteById(id);
    }
}
