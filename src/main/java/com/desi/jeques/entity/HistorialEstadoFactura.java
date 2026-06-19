package com.desi.jeques.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "historial_estado_factura")
public class HistorialEstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private LocalDateTime fechaHora;

    
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

}
