package com.desi.jeques.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.Factura;
import com.desi.jeques.service.ContratoService;
//import com.desi.jeques.entity.Factura;
import com.desi.jeques.service.FacturaService;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    
    @Autowired
    private ContratoService contratoService; 
       
    
    @GetMapping("/nuevaFactura")
    public String mostrarFormulario(@RequestParam("contratoId") Long contratoId, Model modelo) {
        Contrato contratoSeleccionado = contratoService.buscarPorId(contratoId);      
        modelo.addAttribute("contrato", contratoSeleccionado);        
        return "nuevaFactura";
    }
    
    @PostMapping("/nuevaFactura")
    public String crearFactura(@ModelAttribute Factura form, Model model) {
        facturaService.crearFactura(
            form.getContrato().getId(),
            form.getConceptoFacturado(),
            form.getFechaEmision(),
            form.getFechaVencimiento(),
            form.getImporte(),
            form.getFechaPago(),
            form.getMedio(),
            form.getImportePagado(),
            form.getInteres()
        );
        return "redirect:/facturas";
    }
    
    
    @GetMapping("/contratosParaFacturar")
    public String mostrarContratosParaFacturar(Model model) {
        List<Contrato> contratosActivos = contratoService.listarContratosActivos();        
        
        //System.out.println("Cantidad contratos: " + contratosActivos.size());

        /*contratosActivos.forEach(c ->
            System.out.println(c.getId() + " - " + c.getEstado())
        );*/  
        
        model.addAttribute("contratos", contratosActivos);
        return "contratosParaFacturar";
    }
    
    
}