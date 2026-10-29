package br.edu.fatec.grupog.controller;

import br.edu.fatec.grupog.model.Foto;
import br.edu.fatec.grupog.model.Pet;
import br.edu.fatec.grupog.service.FotoService;
import br.edu.fatec.grupog.service.PetService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller
public class PetController {

    private final PetService petService;
    private final FotoService fotoService;

    public PetController(PetService petService, FotoService fotoService) {
        this.petService = petService;
        this.fotoService = fotoService;
    }

    @GetMapping("/")
    public String catalogo(Model model) {
        model.addAttribute("pets", petService.listar());
        return "catalogo";
    }

    @GetMapping("/pets/novo")
    public String formNovo(Model model) {
        model.addAttribute("pet", new Pet());
        return "pet-form";
    }

    @PostMapping("/pets")
    public String criar(Pet pet, RedirectAttributes redirect) {
        petService.salvar(pet);
        redirect.addFlashAttribute("ok", "Pet cadastrado.");
        return "redirect:/";
    }

    @PostMapping("/pets/{id}/excluir")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            petService.excluir(id);
            redirect.addFlashAttribute("ok", "Pet excluido.");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/pets/" + id;
        }
        return "redirect:/";
    }

    @GetMapping("/pets/{id}")
    public String detalhe(@PathVariable("id") Long id, Model model) {
        Pet pet = petService.buscar(id);
        List<Foto> fotos = fotoService.listarDoPet(id);

        Map<LocalDate, List<Foto>> historico = fotos.stream()
                .collect(Collectors.groupingBy(
                        Foto::getDataFoto,
                        () -> new TreeMap<LocalDate, List<Foto>>(Comparator.reverseOrder()),
                        Collectors.toList()));

        model.addAttribute("pet", pet);
        model.addAttribute("fotoPrincipal", pet.getFotoPrincipal());
        model.addAttribute("historico", historico);
        return "detalhe";
    }

    @GetMapping("/pets/{id}/fotos/nova")
    public String formFoto(@PathVariable("id") Long id, Model model) {
        model.addAttribute("pet", petService.buscar(id));
        return "foto-form";
    }

    @PostMapping("/pets/{id}/fotos")
    public String enviarFoto(@PathVariable("id") Long id,
                             @RequestParam("arquivo") MultipartFile arquivo,
                             @RequestParam("dataFoto") LocalDate dataFoto,
                             @RequestParam(value = "principal", required = false) boolean principal,
                             RedirectAttributes redirect) {
        try {
            fotoService.enviar(id, arquivo, dataFoto, principal);
            redirect.addFlashAttribute("ok", "Foto enviada para o bucket.");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/pets/" + id;
    }

    @PostMapping("/pets/{petId}/fotos/{fotoId}/apagar")
    public String apagarFoto(@PathVariable("petId") Long petId,
                             @PathVariable("fotoId") Long fotoId,
                             RedirectAttributes redirect) {
        try {
            fotoService.apagar(petId, fotoId);
            redirect.addFlashAttribute("ok", "Foto apagada.");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/pets/" + petId;
    }

    @PostMapping("/pets/{petId}/fotos/{fotoId}/principal")
    public String tornarPrincipal(@PathVariable("petId") Long petId,
                                  @PathVariable("fotoId") Long fotoId,
                                  RedirectAttributes redirect) {
        try {
            fotoService.tornarPrincipal(petId, fotoId);
            redirect.addFlashAttribute("ok", "Foto principal atualizada.");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/pets/" + petId;
    }

    @GetMapping("/pets/{petId}/fotos/{fotoId}/download")
    public String baixarFoto(@PathVariable("petId") Long petId,
                             @PathVariable("fotoId") Long fotoId,
                             HttpServletResponse response,
                             RedirectAttributes redirect) {
        try {
            fotoService.download(petId, fotoId, response);
            return null;
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/pets/" + petId;
        }
    }
}
