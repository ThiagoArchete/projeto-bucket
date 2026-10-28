package br.edu.fatec.grupog.controller;

import br.edu.fatec.grupog.storage.GcsConfig;
import br.edu.fatec.grupog.storage.GcsObjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BucketController {

    private final GcsObjectService gcsObjectService;

    public BucketController(GcsObjectService gcsObjectService) {
        this.gcsObjectService = gcsObjectService;
    }

    @GetMapping("/bucket")
    public String listarObjetos(Model model) {
        model.addAttribute("objetos", gcsObjectService.listar());
        model.addAttribute("baseUrl", GcsConfig.PUBLIC_BASE_URL);
        return "bucket";
    }
}
