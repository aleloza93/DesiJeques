package com.desi.jeques.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desi.jeques.entity.Contrato;
import com.desi.jeques.entity.Persona;
import com.desi.jeques.entity.Propiedad;
import com.desi.jeques.service.ContratoService;
import com.desi.jeques.service.PersonaService;
import com.desi.jeques.service.PropiedadService;

@Controller
@RequestMapping("/contratos")
public class ContratoController {


	  private final PropiedadService propiedadService;
	  private final PersonaService personaService;
	  private final ContratoService contratoService;
	  

	  public ContratoController(PropiedadService propiedadService, PersonaService personaService,
			ContratoService contratoService) {
		super();
		this.propiedadService = propiedadService;
		this.personaService = personaService;
		this.contratoService = contratoService;
	}


	  @GetMapping
	    public String listar(Model model) {
	        model.addAttribute("contratos", contratoService.listarContratosActivos());
	        return "contratos/listado";
	    }
	  
	  public enum EstadoContrato {
		    BORRADOR,
		    ACTIVO,
		    FINALIZADO,
		    RESCINDIDO
		}
	  
	  @GetMapping("/nuevo")
	  public String nuevo(Model model) {

	      Contrato contrato = new Contrato();
	      
	      contrato.setInquilino(new Persona());
	      contrato.setPropiedad(new Propiedad());
	      contrato.setEstado("Borrador"); // valor inicial

	      model.addAttribute("contrato", contrato);
	      model.addAttribute("propiedades", propiedadService.listarActivas());
	      model.addAttribute("personas", personaService.listarActivas());
	     

	      return "contratos/formulario";
	  }

	    @GetMapping("/editar/{id}")
	    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
	        Contrato contrato = contratoService.buscarPorId(id);

	        if (contrato == null) {
	            redirectAttributes.addFlashAttribute("error", "El contrato no existe.");
	            return "redirect:/contratos";
	        }
	     	     
	        model.addAttribute("contrato", contrato);
	        model.addAttribute("personas", personaService.listarActivas());
	        model.addAttribute("propiedades", propiedadService.listarActivas());
	        model.addAttribute("modoEdicion", true);
	        return "contratos/formulario";
	    }

	    @PostMapping("/guardar")
	    public String guardar(@ModelAttribute Contrato contrato,
	                          Model model,
	                          RedirectAttributes redirectAttributes) {

	        if (contrato.getPropiedad() == null
	                || contrato.getPropiedad().getId() == null
	                || contrato.getInquilino() == null
	                || contrato.getInquilino().getId() == null
	                || contrato.getFechaInicio() == null
	                || contrato.getDuracionMeses() == null
	                || contrato.getDuracionMeses() <= 0
	                || contrato.getImporteMensual() == null
	                || contrato.getImporteMensual().doubleValue() <= 0
	                || contrato.getDiaVencimientoMensual() == null
	                || contrato.getDiaVencimientoMensual() < 1
	                || contrato.getDiaVencimientoMensual() > 31
	                || contrato.getDescripcion() == null
	                || contrato.getDescripcion().isBlank()
	                || contrato.getEstado() == null) {

	            model.addAttribute("error",
	                    "Todos los campos son obligatorios y deben ser válidos.");

	            model.addAttribute("propiedades", propiedadService.listarActivas());
	            model.addAttribute("personas", personaService.listarActivas());
	            model.addAttribute("modoEdicion", contrato.getId() != null);

	            return "contratos/formulario";
	        }

	

	        contratoService.guardar(contrato);

	        redirectAttributes.addFlashAttribute(
	                "mensaje", "Contrato guardado correctamente.");

	        return "redirect:/contratos";
	    }

	    @GetMapping("/eliminar/{id}")
	    public String eliminar(@PathVariable Long id,
	                           RedirectAttributes redirectAttributes) {

	        contratoService.eliminarLogico(id);

	        redirectAttributes.addFlashAttribute(
	                "ok",
	                "Contrato eliminado correctamente.");

	        return "redirect:/contratos";
	    }
	}
	  

