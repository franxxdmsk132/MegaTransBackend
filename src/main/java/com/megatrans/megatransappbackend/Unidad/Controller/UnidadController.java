package com.megatrans.megatransappbackend.Unidad.Controller;

import com.lowagie.text.DocumentException;
import com.megatrans.megatransappbackend.Unidad.Entity.Unidad;
import com.megatrans.megatransappbackend.Unidad.ReporteCamiones;
import com.megatrans.megatransappbackend.Unidad.Service.UnidadService;
import com.megatrans.megatransappbackend.dto.Mensaje;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/unidad")
@CrossOrigin(origins = "http://localhost:4200")
public class UnidadController {

    @Autowired
    UnidadService unidadService;

    @GetMapping("/lista")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('EMPL')")
    public ResponseEntity<List<Unidad>> list() {
        List<Unidad> list = unidadService.getAllUnidades();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('EMPL')")
    public ResponseEntity<Unidad> getById(@PathVariable("id") int id) {
        if (!unidadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Unidad producto = unidadService.getUnidadById(id).get();
        return new ResponseEntity(producto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(

            @RequestParam("imagenUrl") MultipartFile imagenUrl,
            @RequestParam("altura") Double altura,
            @RequestParam("ancho") Double ancho,
            @RequestParam("largo") Double largo,
            @RequestParam("tipo") String tipo,
            @RequestParam("tipo_cajon") String tipo_cajon) {

        // Validaciones
        if (StringUtils.isBlank(tipo)) {
            return new ResponseEntity<>(new Mensaje("El tipo de camión es obligatorio"), HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(tipo_cajon)) {
            return new ResponseEntity<>(new Mensaje("El tipo de cajón es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (altura == null || altura <= 0) {
            return new ResponseEntity<>(new Mensaje("La altura debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }
        if (ancho == null || ancho <= 0) {
            return new ResponseEntity<>(new Mensaje("El ancho debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }
        if (largo == null || largo <= 0) {
            return new ResponseEntity<>(new Mensaje("El largo debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }
        if (imagenUrl == null || imagenUrl.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("La imagen del camión es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        // Guardar la imagen
        String imgUrl;
        try {
            imgUrl = guardarImagen(imagenUrl); // Implementa guardarImagen para manejar la imagen correctamente.
        } catch (Exception e) {
            return new ResponseEntity<>(new Mensaje("Error al guardar la imagen: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }


        // Crear y guardar el camión
        Unidad unidad = new Unidad(imgUrl, altura, largo, ancho, tipo, tipo_cajon);
        unidadService.addUnidad(unidad);

        return new ResponseEntity<>(new Mensaje("Camión creado correctamente"), HttpStatus.OK);
    }

    private String guardarImagen(MultipartFile imagen) {
        try {
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get("uploads").resolve(nombreArchivo).toAbsolutePath();
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + nombreArchivo;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") int id,
            @RequestParam(value = "imagenUrl", required = false) MultipartFile imagenUrl,
            @RequestParam("altura") Double altura,
            @RequestParam("ancho") Double ancho,
            @RequestParam("largo") Double largo,
            @RequestParam("tipo") String tipo,
            @RequestParam("tipo_cajon") String tipo_cajon) {

        // Verificar si el camión existe
        if (!unidadService.existsById(id)) {
            return new ResponseEntity<>(new Mensaje("No existe un camión con ese ID"), HttpStatus.NOT_FOUND);
        }

        // Validaciones de campos
        if (StringUtils.isBlank(tipo)) {
            return new ResponseEntity<>(new Mensaje("El tipo de camión es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(tipo_cajon)) {
            return new ResponseEntity<>(new Mensaje("El tipo de cajón es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (altura == null || altura <= 0) {
            return new ResponseEntity<>(new Mensaje("La altura debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }
        if (ancho == null || ancho <= 0) {
            return new ResponseEntity<>(new Mensaje("El ancho debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }
        if (largo == null || largo <= 0) {
            return new ResponseEntity<>(new Mensaje("El largo debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
        }

        // Obtener el camión existente
        Unidad unidad = unidadService.getUnidadById(id).get();
        unidad.setTipo(tipo);
        unidad.setTipo_cajon(tipo_cajon);
        unidad.setAltura(altura);
        unidad.setAncho(ancho);
        unidad.setLargo(largo);


        // Si se proporciona una nueva imagen, guárdala y actualiza la URL
        if (imagenUrl != null && !imagenUrl.isEmpty()) {
            String imagen = guardarImagen(imagenUrl);
            unidad.setImagenUrl(imagen);
        }

        // Guardar el camión actualizado
        unidadService.addUnidad(unidad);
        return new ResponseEntity<>(new Mensaje("Camión actualizado correctamente"), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        if (!unidadService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        unidadService.deleteUnidad(id);
        return new ResponseEntity(new Mensaje("Camion eliminado"), HttpStatus.OK);
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping("/exportarPDF")
//    public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
//        response.setContentType("application/pdf");
//
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//        String fechaActual = dateFormatter.format(new Date());
//
//        String cabecera = "Content-Disposition";
//        String valor = "attachment; filename=INGRESOS_" + fechaActual + ".pdf";
//
//        response.setHeader(cabecera, valor);
//
//        List<Unidad> unidadList = unidadService.getAllUnidades();
//
//        ReporteCamiones exporter = new ReporteCamiones(unidadList);
//        exporter.exportarReporte(response);
//    }
}
