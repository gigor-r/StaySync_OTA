package com.staysync.ota.messaging;

import com.staysync.ota.model.CanalOta;
import com.staysync.ota.service.CanalOtaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HabitacionEventListener - Tests Unitarios")
class HabitacionEventListenerTest {

    @Mock
    private CanalOtaService canalOtaService;

    @InjectMocks
    private HabitacionEventListener habitacionEventListener;

    private CanalOta canalBooking;

    @BeforeEach
    void setUp() {
        canalBooking = CanalOta.builder()
                .id(1L).nombre("Booking.com").codigo("BOOKING")
                .comision(BigDecimal.valueOf(15)).activo(true).build();
    }

    @Test
    @DisplayName("debe procesar evento de disponibilidad y notificar canales activos")
    void debeProcesarEventoDisponibilidad() {
        Map<String, Object> evento = Map.of(
                "habitacionId", 10,
                "fecha", "2025-03-01",
                "disponible", true
        );
        when(canalOtaService.listarActivos()).thenReturn(List.of(canalBooking));

        habitacionEventListener.onDisponibilidadActualizada(evento);

        verify(canalOtaService).listarActivos();
    }

    @Test
    @DisplayName("debe procesar evento con canales vacíos sin error")
    void debeProcesarConListaCanalesVacia() {
        Map<String, Object> evento = Map.of(
                "habitacionId", 5,
                "fecha", "2025-06-15",
                "disponible", false
        );
        when(canalOtaService.listarActivos()).thenReturn(List.of());

        habitacionEventListener.onDisponibilidadActualizada(evento);

        verify(canalOtaService).listarActivos();
    }

    @Test
    @DisplayName("debe manejar evento con habitacionId inválido sin lanzar excepción")
    void debeManejarHabitacionIdInvalidoSinExcepcion() {
        Map<String, Object> eventoMalformado = Map.of("habitacionId", "no-es-numero");

        habitacionEventListener.onDisponibilidadActualizada(eventoMalformado);

        verify(canalOtaService, never()).listarActivos();
    }
}
