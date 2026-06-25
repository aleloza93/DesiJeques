package com.desi.jeques.controller;

import java.time.LocalDate;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String procesarModificacion(@PathVariable Long id, @ModelAttribute Factura form, Model model) {
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
    
    @GetMapping("/eliminarFactura")
    public String facturasEliminables(Model model) {
    	model.addAttribute("facturas", facturaService.listarEliminables());   
    	return "eliminarFactura";
    }
    
   /*@PostMapping("/eliminarFactura")
    public String procesarEliminarFactura(@RequestParam Long id) {
    	facturaService.eliminarFactura(id);

        return "redirect:/facturas/eliminarFactura";
    }*/
    
    @PostMapping("/eliminarFactura")
    public String procesarEliminarFactura(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {

        boolean eliminado = facturaService.eliminarFactura(id);

        if (eliminado) {
            redirectAttributes.addFlashAttribute(
                "mensaje",
                "Factura eliminada correctamente");
        } else {
            redirectAttributes.addFlashAttribute(
                "error",
                "La factura no puede ser eliminada");
        }

        return "redirect:/facturas/listaFacturas";
    }
    
    /*****************Historia de Usuario 4.4: VER FACTURAS*****************/
   /* @GetMapping("/listaFacturas")
    public String listarFacturas(Model model) {

        List<Factura> facturas = facturaService.facturasNoEliminadas();

        model.addAttribute("facturas", facturas);

        return "listaFacturas";
    }*/
    
    @GetMapping("/listaFacturas")
    public String listarFacturas(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long inquilinoId,
            @RequestParam(required = false) EstadoFactura estado,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            Model model) {

        List<Factura> facturas = facturaService.filtrar(
                contratoId,
                propiedadId,
                inquilinoId,
                estado,
                fechaDesde,
                fechaHasta);

        model.addAttribute("facturas", facturas);

        return "listaFacturas";
    }
}