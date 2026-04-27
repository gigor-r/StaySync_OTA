package com.staysync.ota.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HabitacionEventListener {

    @RabbitListener(queues = "q.ota.sincronizacion")
    public void onDisponibilidadActualizada(Map<String, Object> evento) {
        try {
            Long habitacionId = Long.valueOf(evento.get("habitacionId").toString());
            String fecha      = (String) evento.get("fecha");
            Boolean disponible= (Boolean) evento.get("disponible");
            Object precio     = evento.get("precio");

            log.info("Sincronizando disponibilidad OTA - Habitación: {}, Fecha: {}, Disponible: {}",
                    habitacionId, fecha, disponible);

            // En producción: llamar a cada canal OTA activo via Adapter
            // canalOtaService.listarActivos().forEach(canal ->
            //     adapter.sincronizarDisponibilidad(canal, habitacionId, fecha, disponible, precio));

        } catch (Exception e) {
            log.error("Error procesando evento de disponibilidad OTA: {}", e.getMessage());
        }
    }
}
