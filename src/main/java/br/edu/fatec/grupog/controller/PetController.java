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

    @GetMapping("/pets/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
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
    public String formFoto(@PathVariable Long id, Model model) {
        model.addAttribute("pet", petService.buscar(id));
        return "foto-form";
    }

    @PostMapping("/pets/{id}/fotos")
    public String enviarFoto(@PathVariable Long id,
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
    public String apagarFoto(@PathVariable Long petId,
                             @PathVariable Long fotoId,
                             RedirectAttributes redirect) {
        try {
            fotoService.apagar(petId, fotoId);
            redirect.addFlashAttribute("ok", "Foto apagada.");
        } catch (UnsupportedOperationException e) {
            redirect.addFlashAttribute("erro", "Ainda nao implementado: apagar foto. Tarefa do outro membro.");
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/pets/" + petId;
    }

    @GetMapping("/pets/{petId}/fotos/{fotoId}/download")
    public String baixarFoto(@PathVariable Long petId,
                             @PathVariable Long fotoId,
                             HttpServletResponse response,
                             RedirectAttributes redirect) {
        try {
            fotoService.download(petId, fotoId, response);
            return null;
        } catch (UnsupportedOperationException e) {
            redirect.addFlashAttribute("erro", "Ainda nao implementado: download. Tarefa do outro membro.");
            return "redirect:/pets/" + petId;
        } catch (Exception e) {
            redirect.addFlashAttribute("erro", e.getMessage());
            return "redirect:/pets/" + petId;
        }
    }
}
