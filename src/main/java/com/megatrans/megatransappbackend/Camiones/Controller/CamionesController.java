package com.megatrans.megatransappbackend.Camiones.Controller;

import com.lowagie.text.DocumentException;
import com.megatrans.megatransappbackend.Camiones.Entity.Camiones;
import com.megatrans.megatransappbackend.Camiones.ReporteCamiones;
import com.megatrans.megatransappbackend.Camiones.Service.CamionesService;
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
@RequestMapping("/camiones")
@CrossOrigin(origins = "http://localhost:4200")
public class CamionesController {

    @Autowired
    CamionesService camionesService;

    @GetMapping("/lista")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('EMPL')")
    public ResponseEntity<List<Camiones>> list() {
        List<Camiones> list = camionesService.getAllCamiones();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/detalles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('EMPL')")
    public ResponseEntity<Camiones> getById(@PathVariable("id") int id) {
        if (!camionesService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        Camiones producto = camionesService.getCamionById(id).get();
        return new ResponseEntity(producto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestParam("tipo") String tipo,
            @RequestParam("marca") String marca,
            @RequestParam("modelo") String modelo,
            @RequestParam("placa") String placa,
            @RequestParam("capacidad") Double capacidad,
            @RequestParam("tipo_cajon") String tipo_cajon,
            @RequestParam("altura") Double altura,
            @RequestParam("ancho") Double ancho,
            @RequestParam("largo") Double largo,
            @RequestParam("imagenUrl") MultipartFile imagenUrl,
            @RequestParam("estado") boolean estado)

    {

        // Validaciones
        if (StringUtils.isBlank(tipo)) {
            return new ResponseEntity<>(new Mensaje("El tipo de camión es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(marca)) {
            return new ResponseEntity<>(new Mensaje("La marca es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(modelo)) {
            return new ResponseEntity<>(new Mensaje("El modelo es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(placa) || placa.length() < 6 || placa.length() > 9) {
            return new ResponseEntity<>(new Mensaje("La placa es obligatoria y debe tener entre 6 y 9 caracteres"), HttpStatus.BAD_REQUEST);
        }
        if (capacidad == null || capacidad <= 0) {
            return new ResponseEntity<>(new Mensaje("La capacidad debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
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
        Camiones camiones = new Camiones(tipo, marca, modelo, placa, capacidad, tipo_cajon, altura, ancho, largo, imgUrl, estado);
        camionesService.addCamion(camiones);

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
            @RequestParam("tipo") String tipo,
            @RequestParam("marca") String marca,
            @RequestParam("modelo") String modelo,
            @RequestParam("placa") String placa,
            @RequestParam("capacidad") Double capacidad,
            @RequestParam("tipo_cajon") String tipo_cajon,
            @RequestParam("altura") Double altura,
            @RequestParam("ancho") Double ancho,
            @RequestParam("largo") Double largo,
            @RequestParam(value = "imagenUrl", required = false) MultipartFile imagen,
            @RequestParam("estado") boolean estado) {

        // Verificar si el camión existe
        if (!camionesService.existsById(id)) {
            return new ResponseEntity<>(new Mensaje("No existe un camión con ese ID"), HttpStatus.NOT_FOUND);
        }

        // Validaciones de campos
        if (StringUtils.isBlank(tipo)) {
            return new ResponseEntity<>(new Mensaje("El tipo de camión es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(marca)) {
            return new ResponseEntity<>(new Mensaje("La marca es obligatoria"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(modelo)) {
            return new ResponseEntity<>(new Mensaje("El modelo es obligatorio"), HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(placa) || placa.length() < 6 || placa.length() > 8) {
            return new ResponseEntity<>(new Mensaje("La placa es obligatoria y debe tener entre 6 y 8 caracteres"), HttpStatus.BAD_REQUEST);
        }
        if (capacidad == null || capacidad <= 0) {
            return new ResponseEntity<>(new Mensaje("La capacidad debe ser mayor a 0"), HttpStatus.BAD_REQUEST);
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
        Camiones camiones = camionesService.getCamionById(id).get();
        camiones.setTipo(tipo);
        camiones.setMarca(marca);
        camiones.setModelo(modelo);
        camiones.setPlaca(placa);
        camiones.setCapacidad(capacidad);
        camiones.setTipo_cajon(tipo_cajon);
        camiones.setAltura(altura);
        camiones.setAncho(ancho);
        camiones.setLargo(largo);
        camiones.setEstado(estado);

        // Si se proporciona una nueva imagen, guárdala y actualiza la URL
        if (imagen != null && !imagen.isEmpty()) {
            String imagenUrl = guardarImagen(imagen);
            camiones.setImagenUrl(imagenUrl);
        }

        // Guardar el camión actualizado
        camionesService.addCamion(camiones);
        return new ResponseEntity<>(new Mensaje("Camión actualizado correctamente"), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        if (!camionesService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"), HttpStatus.NOT_FOUND);
        camionesService.deleteCamion(id);
        return new ResponseEntity(new Mensaje("Camion eliminado"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/exportarPDF")
    public void exportarListadoDeEmpleadosEnPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=INGRESOS_" + fechaActual + ".pdf";

        response.setHeader(cabecera, valor);

        List<Camiones> camionesList = camionesService.getAllCamiones();

        ReporteCamiones exporter = new ReporteCamiones(camionesList);
        exporter.exportarReporte(response);
    }
}
