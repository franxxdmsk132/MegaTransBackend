package com.megatrans.megatransappbackend.Security.controller;


import com.megatrans.megatransappbackend.Security.dto.JwtDto;
import com.megatrans.megatransappbackend.Security.dto.LoginUsuario;
import com.megatrans.megatransappbackend.Security.dto.NuevoUsuario;
import com.megatrans.megatransappbackend.Security.entity.Rol;
import com.megatrans.megatransappbackend.Security.entity.Usuario;
import com.megatrans.megatransappbackend.Security.enums.RolNombre;
import com.megatrans.megatransappbackend.Security.jwt.JwtProvider;
import com.megatrans.megatransappbackend.Security.service.RolService;
import com.megatrans.megatransappbackend.Security.service.UsuarioService;
import com.megatrans.megatransappbackend.dto.Mensaje;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String telefono,
            @RequestParam MultipartFile foto,
            @RequestParam String nombreUsuario,
            @RequestParam String password,
            @RequestParam(required = false) String roles) {

        // Validaciones manuales
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || nombreUsuario.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("Campos no pueden estar vacíos"), HttpStatus.BAD_REQUEST);
        }

        if (usuarioService.existsByNombreUsuario(nombreUsuario)) {
            return new ResponseEntity<>(new Mensaje("Ese usuario ya existe"), HttpStatus.BAD_REQUEST);
        }

        if (usuarioService.existsByEmail(email)) {
            return new ResponseEntity<>(new Mensaje("Ese email ya existe"), HttpStatus.BAD_REQUEST);
        }

        // Guardar la imagen
        String fotoUrl = guardarImagenUsuario(foto);

        Usuario usuario = new Usuario(nombre, apellido, email, telefono, fotoUrl, nombreUsuario, passwordEncoder.encode(password));

        // Asignar el rol basado en el parámetro 'roles'
        Set<Rol> rolesAsignados = new HashSet<>();
        if (roles == null || roles.isEmpty()) {
            rolesAsignados.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        } else if (roles.equalsIgnoreCase("admin")) {
            rolesAsignados.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        } else {
            return new ResponseEntity<>(new Mensaje("Rol no válido"), HttpStatus.BAD_REQUEST);
        }
        usuario.setRoles(rolesAsignados);

        usuarioService.save(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario guardado"), HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nuevoEmpleado")
    public ResponseEntity<?> nuevoEmpleado(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("email") String email,
            @RequestParam("telefono") String telefono,
            @RequestParam("foto") MultipartFile foto,
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("password") String password
    ) {

        // Validaciones manuales
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || nombreUsuario.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("Campos no pueden estar vacíos"), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByNombreUsuario(nombreUsuario))
            return new ResponseEntity<>(new Mensaje("ese nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
        if (usuarioService.existsByEmail(email))
            return new ResponseEntity<>(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);

        // Guardar la imagen
        String fotoUrl = guardarImagenUsuario(foto);

        // Crear el usuario con los datos proporcionados y la URL de la imagen
        Usuario usuario = new Usuario(nombre, apellido, email, telefono, fotoUrl, nombreUsuario, passwordEncoder.encode(password));

        // Asignar el rol de empleado
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_EMPL).get()); // Asignar solo el rol ROLE_EMPL
        usuario.setRoles(roles);

        // Guardar el usuario
        usuarioService.save(usuario);

        return new ResponseEntity<>(new Mensaje("empleado guardado"), HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new Mensaje("Campos mal puestos"), HttpStatus.BAD_REQUEST);
        }

        try {
            // Autenticación
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));

            // Establecer autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generar el JWT
            String jwt = jwtProvider.generateToken(authentication);

            // Obtener los detalles del usuario autenticado
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Obtener el usuario desde el servicio
            Usuario usuario = usuarioService.getByNombreUsuario(userDetails.getUsername())
                    .orElseThrow(() -> {
                        logger.error("Usuario no encontrado: {}", userDetails.getUsername());
                        return new RuntimeException("Usuario no encontrado");
                    });

            // Construir la respuesta con el JWT y detalles del usuario
            JwtDto jwtDto = new JwtDto(
                    jwt,
                    userDetails.getUsername(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    usuario.getTelefono(),
                    usuario.getFoto(),
                    userDetails.getAuthorities()  // Usar directamente la colección de authorities
            );

            return new ResponseEntity<>(jwtDto, HttpStatus.OK);
        } catch (AuthenticationException e) {
            logger.error("Error de autenticación: ", e);
            return new ResponseEntity<>(new Mensaje("Credenciales incorrectas"), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            logger.error("Error en el login: ", e);
            return new ResponseEntity<>(new Mensaje("Usuario no encontrado"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error en el servidor: ", e);
            return new ResponseEntity<>(new Mensaje("Error en el servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/empleados")
    public ResponseEntity<List<NuevoUsuario>> getEmpleados() {
        List<Usuario> empleados = usuarioService.getUsuariosByRol(RolNombre.ROLE_EMPL);
        List<NuevoUsuario> empleadosDto = empleados.stream()
                .map(usuario -> {
                    NuevoUsuario nuevoUsuario = new NuevoUsuario();
                    nuevoUsuario.setId(usuario.getId());
                    nuevoUsuario.setNombre(usuario.getNombre());
                    nuevoUsuario.setNombreUsuario(usuario.getNombreUsuario());
                    nuevoUsuario.setEmail(usuario.getEmail());
                    nuevoUsuario.setApellido(usuario.getApellido());
                    nuevoUsuario.setTelefono(usuario.getTelefono());
                    nuevoUsuario.setFoto(usuario.getFoto());
                    nuevoUsuario.setPassword(usuario.getPassword());

                    // Convirtiendo Set<Rol> a Set<String> roles
                    Set<String> roles = usuario.getRoles().stream()
                            .map(rol -> rol.getRolNombre().name())
                            .collect(Collectors.toSet());
                    nuevoUsuario.setRoles(roles);
                    return nuevoUsuario;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(empleadosDto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/empleado/{id}")
    public ResponseEntity<?> actualizarEmpleado(
            @PathVariable Integer id,
            @RequestParam("nombre") String nombre,
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("email") String email,
            @RequestParam("apellido") String apellido,
            @RequestParam("telefono") String telefono,
            @RequestParam("password") String password,
            @RequestParam(value = "foto", required = false) MultipartFile foto) {


        // Validaciones manuales
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || nombreUsuario.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("Campos no pueden estar vacíos"), HttpStatus.BAD_REQUEST);
        }
        if (!usuarioService.existsById(id))
            return new ResponseEntity<>(new Mensaje("el usuario no existe"), HttpStatus.NOT_FOUND);

        Usuario usuario = usuarioService.getById(id).get();

        if (usuarioService.existsByNombreUsuario(nombreUsuario) &&
                !usuario.getNombreUsuario().equals(nombreUsuario))
            return new ResponseEntity<>(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

        if (usuarioService.existsByEmail(email) &&
                !usuario.getEmail().equals(email))
            return new ResponseEntity<>(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        // Si se proporciona una nueva foto, actualizarla
        if (foto != null && !foto.isEmpty()) {
            String fotoUrl = guardarImagenUsuario(foto);
            usuario.setFoto(fotoUrl);
        }
        usuario.setNombreUsuario(nombreUsuario);

        if (password != null && !password.isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(password)); // Si se actualiza la contraseña
        }

        // Actualizar los roles del usuario
        Set<Rol> roles = new HashSet<>();
        if (usuario.getRoles().stream().anyMatch(rol -> rol.getRolNombre().equals(RolNombre.ROLE_ADMIN))) {
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        } else {
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_EMPL).get());
        }
        usuario.setRoles(roles);

        // Guardar el usuario actualizado
        usuarioService.save(usuario);

        return new ResponseEntity<>(new Mensaje("empleado actualizado"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/empleado/{id}")
    public ResponseEntity<?> eliminarEmpleado(@PathVariable Integer id) {
        if (!usuarioService.existsById(id))
            return new ResponseEntity<>(new Mensaje("el usuario no existe"), HttpStatus.NOT_FOUND);

        usuarioService.deleteById(id);
        return new ResponseEntity<>(new Mensaje("empleado eliminado"), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/empleado/{id}")
    public ResponseEntity<NuevoUsuario> buscarEmpleadoPorId(@PathVariable Integer id) {
        if (!usuarioService.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Usuario usuario = usuarioService.getById(id).get();
        NuevoUsuario nuevoUsuario = new NuevoUsuario();
        nuevoUsuario.setId(usuario.getId());
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApellido(usuario.getApellido());
        nuevoUsuario.setEmail(usuario.getEmail());
        nuevoUsuario.setTelefono(usuario.getTelefono());
        nuevoUsuario.setFoto(usuario.getFoto());
        nuevoUsuario.setNombreUsuario(usuario.getNombreUsuario());
        nuevoUsuario.setPassword(usuario.getPassword());

        Set<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getRolNombre().name())
                .collect(Collectors.toSet());
        nuevoUsuario.setRoles(roles);

        return new ResponseEntity<>(nuevoUsuario, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN') or hasRole('EMPL') or hasRole('USER')")
    @GetMapping("/perfil/{username}")
    public ResponseEntity<?> obtenerPerfilPorUsername(@PathVariable String username) {
        // Buscar el usuario en la base de datos
        Usuario usuario = usuarioService.getByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear el DTO de perfil
        NuevoUsuario perfil = new NuevoUsuario();
        perfil.setId(usuario.getId());
        perfil.setNombre(usuario.getNombre());
        perfil.setApellido(usuario.getApellido());
        perfil.setEmail(usuario.getEmail());
        perfil.setTelefono(usuario.getTelefono());
        perfil.setFoto(usuario.getFoto());
        perfil.setNombreUsuario(usuario.getNombreUsuario());
        perfil.setPassword(usuario.getPassword());

        // Convertir roles a formato de String
        Set<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getRolNombre().name())
                .collect(Collectors.toSet());
        perfil.setRoles(roles);

        return new ResponseEntity<>(perfil, HttpStatus.OK);
    }


    // Método para guardar la imagen en el servidor
    private String guardarImagenUsuario(MultipartFile imagen) {
        try {
            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get("usersimg").resolve(nombreArchivo).toAbsolutePath();
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

            return "/usersimg/" + nombreArchivo;
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen", e);
        }
    }


}
