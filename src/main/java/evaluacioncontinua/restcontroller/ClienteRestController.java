package evaluacioncontinua.restcontroller;

import evaluacioncontinua.entity.Cliente;
import evaluacioncontinua.service.IClienteService;
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
@RequestMapping("/api/clientes")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    private final String UPLOAD_DIR = "src/main/resources/uploads/";

    // Listar todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    // Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.editarCliente(id);
        return ResponseEntity.ok(cliente);
    }

    // Crear cliente
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestPart("cliente") Cliente cliente,
                                   BindingResult result,
                                   @RequestPart(value = "foto", required = false) MultipartFile foto) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result.getAllErrors());
        }

        if (clienteService.correoExiste(cliente.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Mensajes.EMAIL_DUPLICADO);
        }

        // Subir foto si existe
        if (foto != null && !foto.isEmpty()) {
            try {
                Path ruta = Paths.get(UPLOAD_DIR + foto.getOriginalFilename());
                Files.createDirectories(ruta.getParent());
                Files.write(ruta, foto.getBytes());
                cliente.setFoto(foto.getOriginalFilename());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al subir la foto: " + e.getMessage());
            }
        }

        Cliente nuevoCliente = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    // Editar cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id,
                                    @Valid @RequestBody Cliente cliente,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result.getAllErrors());
        }

        Cliente existente = clienteService.editarCliente(id);
        existente.setNombre(cliente.getNombre());
        existente.setApellido(cliente.getApellido());
        existente.setEmail(cliente.getEmail());
        existente.setFoto(cliente.getFoto());

        Cliente actualizado = clienteService.crearCliente(existente);
        return ResponseEntity.ok(actualizado);
    }

    // Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.ok(Mensajes.CLIENTE_ELIMINADO);
    }

    // ✅ Buscar por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Cliente>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(clienteService.buscarPorNombre(nombre));
    }

    // ✅ Buscar por apellido
    @GetMapping("/buscar/apellido")
    public ResponseEntity<List<Cliente>> buscarPorApellido(@RequestParam String apellido) {
        return ResponseEntity.ok(clienteService.buscarPorApellido(apellido));
    }

    // ✅ Buscar por email
    @GetMapping("/buscar/email")
    public ResponseEntity<List<Cliente>> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(clienteService.buscarPorEmail(email));
    }

    // ✅ Buscar por nombre, apellido o email (combinado)
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarPorKeyword(@RequestParam String q) {
        return ResponseEntity.ok(clienteService.buscarPorNombreApellidoEmail(q));
    }
}
