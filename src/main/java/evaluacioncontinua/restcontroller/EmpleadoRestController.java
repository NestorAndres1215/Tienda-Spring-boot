package evaluacioncontinua.restcontroller;

import evaluacioncontinua.entity.Empleado;
import evaluacioncontinua.service.IEmpleadoService;
import evaluacioncontinua.util.Mensajes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoRestController {

    @Autowired
    private IEmpleadoService empleadoService;

    private final String UPLOAD_DIR = "src/main/resources/uploads/";

    // Listar todos los empleados
    @GetMapping
    public ResponseEntity<List<Empleado>> listar() {
        return ResponseEntity.ok(empleadoService.listarEmpleados());
    }

    // Obtener empleado por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Empleado empleado = empleadoService.editarEmpleado(id);
        if (empleado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Mensajes.EMPLEADO_NO_ENCONTRADO + id);
        }
        return ResponseEntity.ok(empleado);
    }

    // Crear empleado
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestPart("empleado") Empleado empleado,
                                   BindingResult result,
                                   @RequestPart(value = "foto", required = false) MultipartFile foto) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result.getAllErrors());
        }

        // Subir foto si existe
        if (foto != null && !foto.isEmpty()) {
            try {
                Path ruta = Paths.get(UPLOAD_DIR + foto.getOriginalFilename());
                Files.createDirectories(ruta.getParent());
                Files.write(ruta, foto.getBytes());
                empleado.setFoto(foto.getOriginalFilename());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al subir la foto: " + e.getMessage());
            }
        }

        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
    }

    // Editar empleado
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id,
                                    @Valid @RequestBody Empleado empleado,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result.getAllErrors());
        }

        Empleado existente = empleadoService.editarEmpleado(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Mensajes.EMPLEADO_NO_ENCONTRADO + id);
        }

        existente.setNombre(empleado.getNombre());
        existente.setApellido(empleado.getApellido());
        existente.setEmail(empleado.getEmail());
        existente.setFoto(empleado.getFoto());

        Empleado actualizado = empleadoService.crearEmpleado(existente);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar empleado
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Empleado existente = empleadoService.editarEmpleado(id);
        if (existente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Mensajes.EMPLEADO_NO_ENCONTRADO + id);
        }

        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.ok(Mensajes.CLIENTE_ELIMINADO); // Puedes cambiar el mensaje a EMPLEADO_ELIMINADO
    }

    // ✅ Buscar por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Empleado>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(empleadoService.buscarPorNombre(nombre));
    }

    // ✅ Buscar por apellido
    @GetMapping("/buscar/apellido")
    public ResponseEntity<List<Empleado>> buscarPorApellido(@RequestParam String apellido) {
        return ResponseEntity.ok(empleadoService.buscarPorApellido(apellido));
    }

    // ✅ Buscar por email
    @GetMapping("/buscar/email")
    public ResponseEntity<List<Empleado>> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(empleadoService.buscarPorEmail(email));
    }

    // ✅ Buscar por nombre, apellido o email (combinado)
    @GetMapping("/buscar")
    public ResponseEntity<List<Empleado>> buscarPorKeyword(@RequestParam String q) {
        return ResponseEntity.ok(empleadoService.buscarPorNombreApellidoEmail(q));
    }
}
