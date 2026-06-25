package com.desi.jeques.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.Factura;
import com.desi.jeques.service.ContratoService;
//import com.desi.jeques.entity.Factura;
import com.desi.jeques.service.FacturaService;
import com.desi.jeques.utilidades.EstadoFactura;
import com.desi.jeques.utilidades.MedioPago;

@Controller
@RequestMapping("/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;
    
    @Autowired
    private ContratoService contratoService; 
       
    
    
    /*****************Historia de Usuario 4.1: CREAR FACTURA*****************/
    @GetMapping("/nuevaFactura")
    public String mostrarFormulario(@RequestParam Long contratoId, Model modelo) {
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
    
    /*****************Historia de Usuario 4.2: MODIFICAR FACTURA*****************/
 // Muestra la lista de facturas modificables
    @GetMapping("/modificarFactura")
    public String mostrarListaModificables(Model model) {
        model.addAttribute("facturas", facturaService.listarModificables());
        return "listaFacturasModificables";
    }

    // Muestra el formulario de edición de una factura específica
    @GetMapping("/modificarFactura/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("factura", facturaService.obtenerPorId(id));
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
        return "modificarFactura";
    }

    // Procesa el formulario de edición
    @PostMapping("/modificarFactura/{id}")
    public String procesarModificacion(@PathVariable Long id,
                                        @ModelAttribute Factura form,
                                        Model model) {
        facturaService.modificarFactura(
            id,
            form.getEstado(),
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
    /*****************Historia de Usuario 4.3: ELIMINAR FACTURA*****************/
    
    /*****************Historia de Usuario 4.4: VER FACTURAS*****************/
    
}