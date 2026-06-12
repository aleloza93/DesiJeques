package com.desi.jeques.controller;

import com.desi.jeques.entity.Propiedad;
import com.desi.jeques.service.PersonaService;
import com.desi.jeques.service.PropiedadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/propiedades")
public class PropiedadController {

    private final PropiedadService propiedadService;
    private final PersonaService personaService;

    public PropiedadController(PropiedadService propiedadService, PersonaService personaService) {
        this.propiedadService = propiedadService;
        this.personaService = personaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("propiedades", propiedadService.listarActivas());
        return "propiedades/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Propiedad propiedad = new Propiedad();
        propiedad.setEstadoDisponibilidad("disponible");

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("personas", personaService.listarActivas());
        model.addAttribute("modoEdicion", false);
        return "propiedades/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Propiedad propiedad = propiedadService.buscarPorId(id);

        if (propiedad == null) {
            redirectAttributes.addFlashAttribute("error", "La propiedad no existe.");
            return "redirect:/propiedades";
        }

        model.addAttribute("propiedad", propiedad);
        model.addAttribute("personas", personaService.listarActivas());
        model.addAttribute("modoEdicion", true);
        return "propiedades/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Propiedad propiedad,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        if (propiedad.getDireccion() == null || propiedad.getDireccion().isBlank()
                || propiedad.getCiudad() == null || propiedad.getCiudad().isBlank()
                || propiedad.getTipoPropiedad() == null || propiedad.getTipoPropiedad().isBlank()
                || propiedad.getCantidadAmbientes() == null || propiedad.getCantidadAmbientes() <= 0
                || propiedad.getMetrosCuadrados() == null || propiedad.getMetrosCuadrados() <= 0
                || propiedad.getDescripcion() == null || propiedad.getDescripcion().isBlank()
                || propiedad.getEstadoDisponibilidad() == null || propiedad.getEstadoDisponibilidad().isBlank()
                || propiedad.getPropietario() == null || propiedad.getPropietario().getId() == null) {

            model.addAttribute("error", "Todos los campos son obligatorios y deben ser válidos.");
            model.addAttribute("personas", personaService.listarActivas());
            model.addAttribute("modoEdicion", propiedad.getId() != null);
            return "propiedades/formulario";
        }

        if (propiedadService.existeDuplicada(propiedad.getDireccion(), propiedad.getCiudad(), propiedad.getId())) {
            model.addAttribute("error", "Ya existe una propiedad activa con la misma dirección y ciudad.");
            model.addAttribute("personas", personaService.listarActivas());
            model.addAttribute("modoEdicion", propiedad.getId() != null);
            return "propiedades/formulario";
        }

        propiedadService.guardar(propiedad);
        redirectAttributes.addFlashAttribute("ok", "Propiedad guardada correctamente.");
        return "redirect:/propiedades";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        propiedadService.eliminarLogico(id);
        redirectAttributes.addFlashAttribute("ok", "Propiedad eliminada correctamente.");
        return "redirect:/propiedades";
    }
}