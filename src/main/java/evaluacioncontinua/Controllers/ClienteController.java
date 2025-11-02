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

import evaluacioncontinua.entity.Cliente;
import evaluacioncontinua.service.IClienteService;

@Controller
@RequiredArgsConstructor
public class ClienteController {

    private final IClienteService clienteService;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    // Crear el método para retornar la vista:
    @GetMapping("/listar")
    public String listar(Model model) {
        model.addAttribute("titulo", "Listado de Clientes");
        model.addAttribute("clientes", clienteService.listarClientes());
        return "listar";
    }

    // Crear los métodos para guardar:
    @GetMapping("/form")
    public String crear(Model model) {
        model.addAttribute("titulo", "Formulario de Cliente");
        model.addAttribute("cliente", new Cliente());
        return "form";
    }


    // Post:
    @PostMapping("/form")
    public String guardar(@ModelAttribute Cliente cliente,
                          BindingResult result,
                          Model model,
                          @RequestParam("file") MultipartFile foto) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Formulario del Cliente");
            return "form";
        }

        if (!foto.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(foto.getOriginalFilename());
                Files.write(filePath, foto.getBytes());
                cliente.setFoto(foto.getOriginalFilename());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        clienteService.crearCliente(cliente);
        return "redirect:/listar";
    }

    // Método para agregar la imagen:
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Map<String, Object> model) {
        Cliente cliente = clienteService.editarCliente(id);
        if (cliente == null) {
            throw new ResourceNotFoundException(Mensajes.CLIENTE_NO_ENCONTRADO + id);
        }
        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        return "ver";
    }

    // Creando el método para Editar:
    @GetMapping("/form/{id}")
    public String editar(@PathVariable Long id, Map<String, Object> model) {
        if (id <= 0) {
            return "redirect:/listar";
        }
        Cliente cliente = clienteService.editarCliente(id);
        if (cliente == null) {
            throw new ResourceNotFoundException(Mensajes.CLIENTE_NO_ENCONTRADO + id);
        }
        model.put("titulo", "Editar Cliente");
        model.put("cliente", cliente);
        return "form";
    }

    // Creando el método para Eliminar:
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        if (id > 0) {
            clienteService.eliminarCliente(id);
        }
        return "redirect:/listar";
    }
}