package evaluacioncontinua.Controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import evaluacioncontinua.exception.ResourceNotFoundException;
import evaluacioncontinua.util.Mensajes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import evaluacioncontinua.entity.Empleado;
import evaluacioncontinua.service.IEmpleadoService;

@Controller
@RequiredArgsConstructor
public class EmpleadoController {

	private final  IEmpleadoService empleadoService;
	private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

	@GetMapping("/listar")
	public String listar(Model model) {
		model.addAttribute("titulo", "Listado de Empleados");
		model.addAttribute("empleados", empleadoService.listarEmpleados());
		return "listar1";
	}

	@GetMapping("/form")
	public String crear(Model model) {
		model.addAttribute("titulo", "Formulario de Empleados");
		model.addAttribute("empleado", new Empleado());
		return "form1";
	}

	@PostMapping("/form")
	public String guardar(@ModelAttribute Empleado empleado,
						  BindingResult result,
						  Model model,
						  @RequestParam("file") MultipartFile foto) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario del Empleado");
			return "form1";
		}

		if (!foto.isEmpty()) {
			try {
				Path uploadPath = Paths.get(UPLOAD_DIR);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				Path filePath = uploadPath.resolve(foto.getOriginalFilename());
				Files.write(filePath, foto.getBytes());
				empleado.setFoto(foto.getOriginalFilename());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		empleadoService.crearEmpleado(empleado);
		return "redirect:/listar1";
	}

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Map<String, Object> model) {
		Empleado empleado = empleadoService.editarEmpleado(id);
		if (empleado == null) {
			throw new ResourceNotFoundException(Mensajes.EMPLEADO_NO_ENCONTRADO + id);
		}
		model.put("empleado", empleado);
		model.put("titulo", "Detalle Empleado: " + empleado.getNombre());
		return "ver1";
	}

	@GetMapping("/form/{id}")
	public String editar(@PathVariable Long id, Map<String, Object> model) {
		if (id <= 0) {
			return "redirect:/listar1";
		}
		Empleado empleado = empleadoService.editarEmpleado(id);
		if (empleado == null) {
			throw new ResourceNotFoundException(Mensajes.EMPLEADO_NO_ENCONTRADO + id);
		}
		model.put("titulo", "Editar Empleado");
		model.put("empleado", empleado);
		return "form1";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable Long id) {
		if (id > 0) {
			empleadoService.eliminarEmpleado(id);
		}
		return "redirect:/listar1";
	}
}
