package com.staysync.ota.messaging;

import com.staysync.ota.service.CanalOtaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class HabitacionEventListener {

    private final CanalOtaService canalOtaService;

    @RabbitListener(queues = "q.ota.sincronizacion")
    public void onDisponibilidadActualizada(Map<String, Object> evento) {
        try {
            Long habitacionId = Long.valueOf(evento.get("habitacionId").toString());
            String fecha      = (String) evento.get("fecha");
            Boolean disponible = (Boolean) evento.get("disponible");

            log.info("Sincronizando disponibilidad OTA - Habitación: {}, Fecha: {}, Disponible: {}",
                    habitacionId, fecha, disponible);

            canalOtaService.listarActivos().forEach(canal ->
                log.info("Canal OTA [{}] - actualizando disponibilidad habitación {} para {}",
                        canal.getCodigo(), habitacionId, fecha));

        } catch (Exception e) {
            log.error("Error procesando evento de disponibilidad OTA: {}", e.getMessage());
        }
    }
}
