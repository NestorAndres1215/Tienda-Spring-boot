package evaluacioncontinua.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/app", "/"})
public class IndexController {

	private static final String BIENVENIDA = "BIENVENIDO A LA ACADEMIA DEPORTIVA ANDRES";
	private static final String PARRAFO = """
            Lorem, ipsum dolor sit amet consectetur adipisicing elit. Molestiae explicabo sit illo. 
            Et distinctio odit nihil totam quibusdam? Accusamus veniam blanditiis cum nulla neque! 
            Voluptatibus sit atque perspiciatis quibusdam voluptates.
            Lorem ipsum dolor, sit amet consectetur adipisicing elit. Minus, eos ab perferendis, sunt dicta optio adipisci 
            quae suscipit tempora inventore explicabo in quos, deserunt ad magnam qui excepturi libero. Exercitationem.
            """;
	private static final String SUBTITULO = "Los mejores profesores";

	@GetMapping({"/index", "/"})
	public String index(ModelMap model) {
		model.addAttribute("titulo", BIENVENIDA);
		model.addAttribute("parrafo", PARRAFO);
		model.addAttribute("subtitulo", SUBTITULO);
		return "index"; // index.html en templates
	}

	@GetMapping("/consultar")
	public String consultar(ModelMap model) {
		return "consultar"; // consultar.html en templates
	}
}