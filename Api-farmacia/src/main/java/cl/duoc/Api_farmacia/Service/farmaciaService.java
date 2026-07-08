package cl.duoc.Api_farmacia.Service;

import cl.duoc.farmaciaService.Model.RecetaInsumoModel;
import cl.duoc.farmaciaService.Repository.RecetaInsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class farmaciaService {

    @Service

        private final farmaciaRepository FarmaciaRepository;

        public PharmacyService(farmaciaRepository FarmaciaRepository) {
            this.farmaciaRepository = FarmaciaRepository;
        }

        @Transactional
        public FarmaciaRepository procesarPedido(FarmaciaRepository solicitud) {
            if (solicitud.getPacienteId() == null || solicitud.getCamaId() == null) {
                throw new IllegalArgumentException("La solicitud debe incluir de forma obligatoria el ID del Paciente y de la Cama.");
            }
            solicitud.setEstado("PREPARANDO");
            solicitud.setFechaSolicitud(LocalDateTime.now());
            return farmaciaRepository.save(solicitud);
        }

        public List<farmaciaModel> obtenerTodosLosPedidos() {
            return recetaInsumoRepository.findAll();
        }

        public Optional<RecetaInsumoModel> obtenerPedidoPorId(Long id) {
            return farmaciaRepository.findById(id);
        }

        @Transactional
        public Optional<FarmaciaRepository> actualizarEstadoPedido(Long id, String nuevoEstado) {
            String estadoUpper = nuevoEstado.toUpperCase();
            if (!estadoUpper.equals("PREPARANDO") && !estadoUpper.equals("DESPACHADO") && !estadoUpper.equals("ENTREGADO")) {
                throw new IllegalArgumentException("Estado de despacho inválido. Valores permitidos: PREPARANDO, DESPACHADO, ENTREGADO.");
            }
            return farmaciaRepository.findById(id).map(pedido -> {
                pedido.setEstado(estadoUpper);
                return farmaciaRepository.save(pedido);
            });
        }

}
