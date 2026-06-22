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
//import com.desi.jeques.entity.Factura;
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
        return "nuevaFactura";
    }
    
    @PostMapping("/nuevaFactura")
    public String crearFactura(@ModelAttribute FacturaForm form, Model model) {
        facturaService.crearFactura(
            form.getContratoId(),
            form.getConceptoFacturado(),
            form.getFechaEmision(),
            form.getFechaVencimiento(),
            form.getImporte()
        );
        return "redirect:/facturas";
    }
    
    
    @GetMapping("/contratosParaFacturar")
    public String mostrarContratosParaFacturar(Model model) {
        List<Contrato2> contratosActivos = contratoService.obtenerActivos();        
        
        //System.out.println("Cantidad contratos: " + contratosActivos.size());

        /*contratosActivos.forEach(c ->
            System.out.println(c.getId() + " - " + c.getEstado())
        );*/  
        
        model.addAttribute("contratos", contratosActivos);
        return "contratosParaFacturar";
    }
    
    
}