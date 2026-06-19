package com.desi.jeques.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

enum EstadoFactura {
    PENDIENTE,
    PAGADA,
    VENCIDA,
    ANULADA
}

enum MedioPago {
    TRANSFERENCIA,
    EFECTIVO,
    DEBITO,
    CREDITO
}

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEmision;
    private LocalDate fechaVencimiento;
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    private boolean eliminada;

    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    private MedioPago medio;

    private BigDecimal importePagado;
    private BigDecimal interes;
    private String conceptoFacturado;

/*
    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;
*/   
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<HistorialEstadoFactura> historialEstados;


}
