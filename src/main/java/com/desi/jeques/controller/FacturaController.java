package com.desi.jeques.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.desi.jeques.entity.Contrato2;
import com.desi.jeques.entity.Factura;
import com.desi.jeques.service.FacturaService;
import com.desi.jeques.service.impl.Contrato2Service;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    
    @Autowired
    private Contrato2Service contratoService;
    
    
    @GetMapping("/nuevaFactura")
    public String mostrarFormulario(Model modelo) {
        List<Contrato2> contratosActivos = contratoService.obtenerActivos(); //Cambiar ACA contratos2 y funcion de buscarActivos
        modelo.addAttribute("contratos", contratosActivos);
        return "FacturaForm";
    }
    
    @PostMapping("/nuevaFactura")
    public String crearFactura(@ModelAttribute CrearFacturaRequest request, Model model) {
        facturaService.crearFactura(
            request.getContratoId(),
            request.getConceptoFacturado(),
            request.getFechaEmision(),
            request.getFechaVencimiento(),
            request.getImporte()
        );
        return "redirect:/factura";
    }
    
}