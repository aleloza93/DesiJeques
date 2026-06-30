package com.desi.jeques.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desi.jeques.entity.Propiedad;
import com.desi.jeques.entity.Publicacion;
import com.desi.jeques.service.PropiedadService;
import com.desi.jeques.service.PublicacionService;

@Controller
@RequestMapping("/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;
    private final PropiedadService propiedadService;

    public PublicacionController(PublicacionService publicacionService,
                                  PropiedadService propiedadService) {
        this.publicacionService = publicacionService;
        this.propiedadService = propiedadService;
    }

    /***************** Historia de Usuario 2.4: LISTAR PUBLICACIONES *****************/

    @GetMapping
    public String listar(@RequestParam(required = false) Long propiedadId,
                          @RequestParam(required = false) String ciudad,
                          @RequestParam(required = false) String estado,
                          @RequestParam(required = false) BigDecimal precioMin,
                          @RequestParam(required = false) BigDecimal precioMax,
                          Model model) {

        if (estado != null && estado.isBlank()) {
            estado = null;
        }
        if (ciudad != null && ciudad.isBlank()) {
            ciudad = null;
        }

        model.addAttribute("publicaciones",
                publicacionService.listarConFiltros(propiedadId, ciudad, estado, precioMin, precioMax));

        model.addAttribute("propiedades", propiedadService.listarActivas());

        model.addAttribute("propiedadId", propiedadId);
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("estado", estado);
        model.addAttribute("precioMin", precioMin);
        model.addAttribute("precioMax", precioMax);

        return "publicaciones/listado";
    }

    /***************** Historia de Usuario 2.1: ALTA DE PUBLICACION *****************/

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Publicacion publicacion = new Publicacion();
        publicacion.setEstado("activa");
        publicacion.setPropiedad(new Propiedad());

        model.addAttribute("publicacion", publicacion);
        model.addAttribute("propiedades", propiedadesDisponibles());
        model.addAttribute("modoEdicion", false);
        return "publicaciones/formulario";
    }

    /***************** Historia de Usuario 2.3: MODIFICACION DE PUBLICACION *****************/

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Publicacion publicacion = publicacionService.buscarPorId(id);

        if (publicacion == null) {
            redirectAttributes.addFlashAttribute("error", "La publicación no existe.");
            return "redirect:/publicaciones";
        }

        model.addAttribute("publicacion", publicacion);
        model.addAttribute("modoEdicion", true);
        return "publicaciones/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Publicacion publicacion,
                          Model model,
                          RedirectAttributes redirectAttributes) {

        boolean esNueva = publicacion.getId() == null;

        if (publicacion.getPropiedad() == null || publicacion.getPropiedad().getId() == null
                || publicacion.getPrecioMensual() == null
                || publicacion.getPrecioMensual().doubleValue() <= 0
                || publicacion.getFechaPublicacion() == null
                || publicacion.getEstado() == null || publicacion.getEstado().isBlank()
                || publicacion.getCondicionesAlquiler() == null
                || publicacion.getCondicionesAlquiler().isBlank()
                || publicacion.getDescripcion() == null
                || publicacion.getDescripcion().isBlank()) {

            model.addAttribute("error", "Todos los campos son obligatorios y deben ser válidos.");
            model.addAttribute("propiedades", esNueva ? propiedadesDisponibles() : null);
            model.addAttribute("modoEdicion", !esNueva);
            return "publicaciones/formulario";
        }

        try {
            publicacionService.guardar(publicacion);
            redirectAttributes.addFlashAttribute("ok", "Publicación guardada correctamente.");
            return "redirect:/publicaciones";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("propiedades", esNueva ? propiedadesDisponibles() : null);
            model.addAttribute("modoEdicion", !esNueva);
            return "publicaciones/formulario";
        }
    }

    /***************** Historia de Usuario 2.2: ELIMINACION DE PUBLICACION *****************/

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            publicacionService.eliminarLogico(id);
            redirectAttributes.addFlashAttribute("ok", "Publicación eliminada correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/publicaciones";
    }

    /***************** Helper *****************/

    // Propiedades activas y con estado "disponible", para el alta de una publicación.
    private List<Propiedad> propiedadesDisponibles() {
        return propiedadService.listarActivas().stream()
                .filter(p -> "disponible".equals(p.getEstadoDisponibilidad()))
                .collect(Collectors.toList());
    }
}
