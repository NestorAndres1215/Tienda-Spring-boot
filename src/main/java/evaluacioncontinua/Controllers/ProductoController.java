package evaluacioncontinua.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import evaluacioncontinua.exception.ResourceNotFoundException;
import evaluacioncontinua.util.Mensajes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import evaluacioncontinua.entity.Producto;
import evaluacioncontinua.service.IProductoService;

@Controller
@RequiredArgsConstructor
public class ProductoController {

	private final IProductoService productoService;
	private static final String UPLOAD_DIR = "src/main/resources/static/uploads";
	private static final List<String> EXTENSIONES_NO_PERMITIDAS = Arrays.asList(
			".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx"
	);

	@GetMapping("/listar2")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de Productos");
		model.addAttribute("productos", productoService.listarProductos());
		return "listar2";
	}

	@GetMapping("/form2")
	public String crear(Model model) {
		model.addAttribute("titulo", "Formulario de Productos");
		model.addAttribute("producto", new Producto());
		return "form2";
	}

	@PostMapping("/form2")
	public String guardar(@ModelAttribute Producto producto,
						  BindingResult result,
						  Model model,
						  @RequestParam("file") MultipartFile foto) {

		model.addAttribute("titulo", "Formulario del Producto");
		producto.setCreateAt(LocalDate.now());

		// Validación de precio
		if (producto.getPrecio() <= 0.0) {
			model.addAttribute("error", "Agregue un precio válido al producto");
			return "form2";
		}

		if (result.hasErrors()) {
			return "form2";
		}

		// Validación y subida de imagen
		if (!foto.isEmpty()) {
			String fileName = foto.getOriginalFilename();
			String contentType = foto.getContentType();

			boolean extensionNoValida = EXTENSIONES_NO_PERMITIDAS.stream()
					.anyMatch(ext -> fileName != null && fileName.toLowerCase().endsWith(ext));

			boolean tipoNoValido = contentType != null && (contentType.startsWith("application/"));

			if (extensionNoValida || tipoNoValido) {
				model.addAttribute("error", "Solo se permiten archivos de imagen.");
				return "form2";
			}

			try {
				Path uploadPath = Paths.get(UPLOAD_DIR);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				Path filePath = uploadPath.resolve(fileName);
				Files.write(filePath, foto.getBytes());
				producto.setFoto(fileName);
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("error", "Error al guardar la imagen.");
				return "form2";
			}
		}

		productoService.crearProducto(producto);
		return "redirect:/listar2";
	}

	@GetMapping("/ver2/{id}")
	public String ver(@PathVariable Long id, Map<String, Object> model) {
		Producto producto = productoService.editarProducto(id);
		if (producto == null) {
			throw new ResourceNotFoundException(Mensajes.PRODUCTO_NO_ENCONTRADO + id);
		}
		model.put("producto", producto);
		model.put("titulo", "Detalle Producto: " + producto.getNombre());
		return "ver2";
	}

	@GetMapping("/form2/{id}")
	public String editar(@PathVariable Long id, Map<String, Object> model) {
		if (id <= 0) {
			return "redirect:/listar2";
		}
		Producto producto = productoService.editarProducto(id);
		if (producto == null) {
			throw new ResourceNotFoundException(Mensajes.PRODUCTO_NO_ENCONTRADO + id);
		}
		model.put("titulo", "Editar Producto");
		model.put("producto", producto);
		model.put("id", id);
		return "form2";
	}

	@GetMapping("/eliminar2/{id}")
	public String eliminar(@PathVariable Long id) {
		if (id > 0) {
			productoService.eliminarProducto(id);
		}
		return "redirect:/listar2";
	}
}