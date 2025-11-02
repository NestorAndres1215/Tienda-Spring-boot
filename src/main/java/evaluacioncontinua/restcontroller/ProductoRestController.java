package evaluacioncontinua.restcontroller;


import evaluacioncontinua.entity.Producto;
import evaluacioncontinua.service.IProductoService;
import evaluacioncontinua.util.Mensajes;
import evaluacioncontinua.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoRestController {

    private final IProductoService productoService;

    private final String UPLOAD_DIR = "src/main/resources/uploads/";

    // ✅ Listar todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    // ✅ Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerPorId(id);
            return ResponseEntity.ok(producto);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Crear producto
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestPart("producto") Producto producto,
                                   BindingResult result,
                                   @RequestPart(value = "foto", required = false) MultipartFile foto) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        producto.setCreateAt(LocalDate.now());

        // Subir foto opcional
        if (foto != null && !foto.isEmpty()) {
            try {
                Path ruta = Paths.get(UPLOAD_DIR + foto.getOriginalFilename());
                Files.createDirectories(ruta.getParent());
                Files.write(ruta, foto.getBytes());
                producto.setFoto(foto.getOriginalFilename());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al subir la foto: " + e.getMessage());
            }
        }

        Producto nuevoProducto = productoService.crearProducto(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // ✅ Editar producto
    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Long id,
                                    @Valid @RequestBody Producto producto,
                                    BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        try {
            Producto existente = productoService.editarProducto(id);
            existente.setNombre(producto.getNombre());
            existente.setCategoria(producto.getCategoria());
            existente.setPrecio(producto.getPrecio());
            existente.setFoto(producto.getFoto());

            Producto actualizado = productoService.crearProducto(existente);
            return ResponseEntity.ok(actualizado);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // ✅ Buscar por nombre
    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // ✅ Buscar por categoría
    @GetMapping("/buscar/categoria")
    public ResponseEntity<List<Producto>> buscarPorCategoria(@RequestParam String categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    // ✅ Buscar por nombre o categoría combinados
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombreOCategoria(@RequestParam String q) {
        return ResponseEntity.ok(productoService.buscarPorNombreOCategoria(q));
    }

    // ✅ Top 10 por precio descendente
    @GetMapping("/top10/precio/desc")
    public ResponseEntity<List<Producto>> top10Desc() {
        return ResponseEntity.ok(productoService.top10PorPrecioDesc());
    }

    // ✅ Top 10 por precio ascendente
    @GetMapping("/top10/precio/asc")
    public ResponseEntity<List<Producto>> top10Asc() {
        return ResponseEntity.ok(productoService.top10PorPrecioAsc());
    }

}
