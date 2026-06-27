package com.desi.jeques.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import com.desi.jeques.utilidades.FacturaExcepcion;
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
        return "facturas/nuevaFactura";
    }
       
    
    @PostMapping("/nuevaFactura")
    public String crearFactura(
            @RequestParam Long contratoId,
            @ModelAttribute Factura form,
            Model model) {
        try {
            facturaService.crearFactura(
                contratoId,
                form.getConceptoFacturado(),
                form.getFechaEmision(),
                form.getFechaVencimiento(),
                form.getFechaPago(),
                form.getMedio(),
                form.getImportePagado(),
                form.getInteres()
            );
            return "redirect:/facturas";

        } catch (FacturaExcepcion e) {
            if (e.getAtributo() == null) {
                model.addAttribute("errorGlobal", e.getMessage());
            } else {
                model.addAttribute("error_" + e.getAtributo(), e.getMessage());
            }            
            model.addAttribute("contrato", contratoService.buscarPorId(contratoId));
            return "facturas/nuevaFactura";
        }
    }
    
    
    @GetMapping("/contratosParaFacturar")
    public String mostrarContratosParaFacturar(Model model) {
        List<Contrato> contratosActivos = contratoService.obtenerActivos();   
        model.addAttribute("contratos", contratosActivos);
        return "facturas/contratosParaFacturar";
    }
    
    /*****************Historia de Usuario 4.2: MODIFICAR FACTURA*****************/  

    @GetMapping("/modificarFactura/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("factura", facturaService.obtenerPorId(id));
        model.addAttribute("estados", EstadoFactura.values());
        model.addAttribute("mediosPago", MedioPago.values());
        return "facturas/modificarFactura";
    }

    @PostMapping("/modificarFactura/{id}")
    public String procesarModificacion(@PathVariable Long id, @ModelAttribute Factura form, BindingResult result, Model model) {    	
    	
    	if (result.hasErrors()) {            
            model.addAttribute("estados", EstadoFactura.values());
            model.addAttribute("mediosPago", MedioPago.values());
            return "facturas/modificarFactura";
        }

        try {
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
            return "redirect:/facturas/listaFacturas";

        } catch (FacturaExcepcion e) {
            if (e.getAtributo() == null) {
                model.addAttribute("errorGlobal", e.getMessage());
            } else {
                model.addAttribute("error_" + e.getAtributo(), e.getMessage());
            }
            model.addAttribute("factura", facturaService.obtenerPorId(id));
            model.addAttribute("estados", EstadoFactura.values());
            model.addAttribute("mediosPago", MedioPago.values());
            return "facturas/modificarFactura";
        }
    }    	
    	
    
    /*****************Historia de Usuario 4.3: ELIMINAR FACTURA*****************/
    
    @GetMapping("/eliminarFactura")
    public String facturasEliminables(Model model) {
    	model.addAttribute("facturas", facturaService.listarEliminables());   
    	return "facturas/eliminarFactura";
    }  
   
    @PostMapping("/eliminarFactura")
    public String procesarEliminarFactura(
            @RequestParam Long id,
            RedirectAttributes redirectAttributes) {

        try {
            facturaService.eliminarFactura(id);
            redirectAttributes.addFlashAttribute("mensaje", "Factura eliminada correctamente.");
        } catch (FacturaExcepcion e) {
            redirectAttributes.addFlashAttribute("errorGlobal", e.getMessage());
        }

        return "redirect:/facturas/listaFacturas";
    }
    
    /*****************Historia de Usuario 4.4: VER FACTURAS*****************/   
    
    @GetMapping("/listaFacturas")
    public String listarFacturas(
            @RequestParam(required = false) Long contratoId,
            @RequestParam(required = false) Long propiedadId,
            @RequestParam(required = false) Long inquilinoId,
            @RequestParam(required = false) EstadoFactura estado,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta,
            Model model) {

        List<Factura> facturas = facturaService.filtroFactura(
                contratoId,
                propiedadId,
                inquilinoId,
                estado,
                fechaDesde,
                fechaHasta);

        model.addAttribute("facturas", facturas);

        return "facturas/listaFacturas";
    }
}