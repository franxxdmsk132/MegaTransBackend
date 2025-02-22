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
import org.springframework.security.authentication.BadCredentialsException;
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
import java.util.Map;
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
            @RequestParam String nombreComercial,
            @RequestParam String identificacion,
            @RequestParam String telefono,
            @RequestParam String nombreUsuario,
            @RequestParam String password,
            @RequestParam(required = false) String roles) {

        // Validaciones manuales
        if (nombre.isEmpty() || apellido.isEmpty() || identificacion.isEmpty() || telefono.isEmpty() || nombreUsuario.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("Campos no pueden estar vacíos"), HttpStatus.BAD_REQUEST);
        }

        if (usuarioService.existsByNombreUsuario(nombreUsuario)) {
            return new ResponseEntity<>(new Mensaje("Ese usuario ya existe"), HttpStatus.BAD_REQUEST);
        }

//        if (usuarioService.existsByNombreComercial(nombreComercial)) {
//            return new ResponseEntity<>(new Mensaje("Ese nombre Comercial ya existe"), HttpStatus.BAD_REQUEST);
//        }



        Usuario usuario = new Usuario(nombre, apellido, identificacion, telefono, nombreComercial,nombreUsuario, passwordEncoder.encode(password));

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
            @RequestParam("identificacion") String identificacion,
            @RequestParam("telefono") String telefono,
            @RequestParam("nombreComercial") String nombre_comercial,
            @RequestParam("nombreUsuario") String nombreUsuario,
            @RequestParam("password")  String password


            ) {

        // Validaciones manuales
        if (nombre.isEmpty() || apellido.isEmpty() || identificacion.isEmpty() || telefono.isEmpty() || nombreUsuario.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("Campos no pueden estar vacíos"), HttpStatus.BAD_REQUEST);
        }
        if (usuarioService.existsByNombreUsuario(nombreUsuario))
            return new ResponseEntity<>(new Mensaje("ese nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
//        if (usuarioService.existsByNombreComercial(nombre_comercial))
//            return new ResponseEntity<>(new Mensaje("ese nombre Comercial ya existe"), HttpStatus.BAD_REQUEST);


        // Crear el usuario con los datos proporcionados y la URL de la imagen
        Usuario usuario = new Usuario(nombre, apellido, identificacion, telefono,nombre_comercial, nombreUsuario, passwordEncoder.encode(password));

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
                        // Si el usuario no existe
                        logger.error("Usuario no encontrado: {}", userDetails.getUsername());
                        return new RuntimeException("Usuario no encontrado"); // Este bloque es importante para manejar el caso
                    });

            // Construir la respuesta con el JWT y detalles del usuario
            JwtDto jwtDto = new JwtDto(
                    jwt,
                    userDetails.getUsername(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getIdentificacion(),
                    usuario.getTelefono(),
                    usuario.getNombreComercial(),
                    userDetails.getAuthorities()  // Usar directamente la colección de authorities
            );

            return new ResponseEntity<>(jwtDto, HttpStatus.OK);  // Login exitoso

        } catch (BadCredentialsException e) {
            // Si la contraseña es incorrecta
            logger.error("Credenciales incorrectas: ", e);
            return new ResponseEntity<>(new Mensaje("Contraseña incorrecta"), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            // Si el usuario no existe o hay un error al obtenerlo
            logger.error("Usuario no encontrado: ", e);
            return new ResponseEntity<>(new Mensaje("Usuario no registrado"), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Si hay cualquier otro error
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
                    nuevoUsuario.setIdentificacion(usuario.getIdentificacion());
                    nuevoUsuario.setApellido(usuario.getApellido());
                    nuevoUsuario.setNombreComercial(usuario.getNombreComercial());
                    nuevoUsuario.setTelefono(usuario.getTelefono());
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
            @RequestParam("identificacion") String identificacion,
            @RequestParam("apellido") String apellido,
            @RequestParam("telefono") String telefono,
            @RequestParam("nombreComercial") String nombre_comercial,
            @RequestParam("password") String password
//            @RequestParam(value = "foto", required = false) MultipartFile foto
    ) {


        // Validaciones manuales
        if (nombre.isEmpty() || apellido.isEmpty() || identificacion.isEmpty() || telefono.isEmpty() || nombreUsuario.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(new Mensaje("Campos no pueden estar vacíos"), HttpStatus.BAD_REQUEST);
        }
        if (!usuarioService.existsById(id))
            return new ResponseEntity<>(new Mensaje("el usuario no existe"), HttpStatus.NOT_FOUND);

        Usuario usuario = usuarioService.getById(id).get();

        if (usuarioService.existsByNombreUsuario(nombreUsuario) &&
                !usuario.getNombreUsuario().equals(nombreUsuario))
            return new ResponseEntity<>(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);

//        if (usuarioService.existsByNombreComercial(nombre_comercial) &&
//                !usuario.getNombreComercial().equals(nombre_comercial))
//            return new ResponseEntity<>(new Mensaje("ese nombre comercial ya existe"), HttpStatus.BAD_REQUEST);

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setIdentificacion(identificacion);
        usuario.setTelefono(telefono);
        usuario.setNombreComercial(nombre_comercial);
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
        nuevoUsuario.setIdentificacion(usuario.getIdentificacion());
        nuevoUsuario.setTelefono(usuario.getTelefono());
        nuevoUsuario.setNombreComercial(usuario.getNombreComercial());
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
    public ResponseEntity<?> obtenerPerfil(@PathVariable String username) {
        // Obtener el usuario desde el servicio usando el nombre de usuario
        Usuario usuario = usuarioService.getByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Construir el objeto de respuesta con la información del usuario
        NuevoUsuario perfil = new NuevoUsuario();
        perfil.setId(usuario.getId());
        perfil.setNombre(usuario.getNombre());
        perfil.setApellido(usuario.getApellido());
        perfil.setNombreComercial(usuario.getNombreComercial());
        perfil.setIdentificacion(usuario.getIdentificacion());
        perfil.setTelefono(usuario.getTelefono());
        perfil.setNombreUsuario(usuario.getNombreUsuario());

        // Convirtiendo Set<Rol> a Set<String> para devolver los roles del usuario
        Set<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getRolNombre().name())
                .collect(Collectors.toSet());
        perfil.setRoles(roles);

        // Devolver la información del perfil en la respuesta
        return new ResponseEntity<>(perfil, HttpStatus.OK);
    }



//    // Método para guardar la imagen en el servidor
//    private String guardarImagenUsuario(MultipartFile imagen) {
//        try {
//            String nombreArchivo = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
//            Path rutaArchivo = Paths.get("usersimg").resolve(nombreArchivo).toAbsolutePath();
//            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
//
//            return "/usersimg/" + nombreArchivo;
//        } catch (IOException e) {
//            throw new RuntimeException("Error al guardar la imagen", e);
//        }
//    }
@PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('EMPL')")
@PutMapping("/changePass/{username}")
public ResponseEntity<?> cambiarContrasena(
        @PathVariable String username,
        @RequestBody Map<String, String> requestBody // Se recibe un JSON con password y newPassword
) {
    String password = requestBody.get("password");
    String newPassword = requestBody.get("newPassword");

    if (password == null || newPassword == null || password.isEmpty() || newPassword.isEmpty()) {
        return new ResponseEntity<>(new Mensaje("Las contraseñas no pueden estar vacías"), HttpStatus.BAD_REQUEST);
    }

    Usuario usuario = usuarioService.getByNombreUsuario(username).orElse(null);
    if (usuario == null) {
        return new ResponseEntity<>(new Mensaje("Usuario no encontrado"), HttpStatus.NOT_FOUND);
    }

    if (!passwordEncoder.matches(password, usuario.getPassword())) {
        return new ResponseEntity<>(new Mensaje("Contraseña actual incorrecta"), HttpStatus.FORBIDDEN);
    }

    if (newPassword.length() < 6) {
        return new ResponseEntity<>(new Mensaje("La nueva contraseña debe tener al menos 6 caracteres"), HttpStatus.BAD_REQUEST);
    }

    usuario.setPassword(passwordEncoder.encode(newPassword));
    usuarioService.save(usuario);

    return new ResponseEntity<>(new Mensaje("Contraseña actualizada con éxito"), HttpStatus.OK);
}


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('EMPL')")
    @PutMapping("/usuario/{username}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable String username,
            @RequestBody Usuario usuarioActualizado
    ) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioService.getByNombreUsuario(username).orElse(null);
        if (usuario == null) {
            return new ResponseEntity<>(new Mensaje("Usuario no encontrado"), HttpStatus.NOT_FOUND);
        }

        // Actualizar los campos del usuario con los valores del request body
        if (usuarioActualizado.getNombre() != null && !usuarioActualizado.getNombre().isEmpty())
            usuario.setNombre(usuarioActualizado.getNombre());
        if (usuarioActualizado.getApellido() != null && !usuarioActualizado.getApellido().isEmpty())
            usuario.setApellido(usuarioActualizado.getApellido());
        if (usuarioActualizado.getIdentificacion() != null && !usuarioActualizado.getIdentificacion().isEmpty())
            usuario.setIdentificacion(usuarioActualizado.getIdentificacion());
        if (usuarioActualizado.getTelefono() != null && !usuarioActualizado.getTelefono().isEmpty())
            usuario.setTelefono(usuarioActualizado.getTelefono());
        if (usuarioActualizado.getNombreComercial() != null && !usuarioActualizado.getNombreComercial().isEmpty())
            usuario.setNombreComercial(usuarioActualizado.getNombreComercial());
        // Guardar los cambios en la base de datos
        usuarioService.save(usuario);
        return new ResponseEntity<>(new Mensaje("Usuario actualizado correctamente"), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<String> obtenerMensaje() {
        String mensaje = "Hola SSL";
        return ResponseEntity.ok(mensaje);
    }
    // Endpoint para obtener el conteo de usuarios
    @GetMapping("/count")
    public long getUsuariosCount() {
        return usuarioService.countUsuarios();
    }

}
