package br.edu.fatec.grupog.service;

import br.edu.fatec.grupog.model.Foto;
import br.edu.fatec.grupog.model.Pet;
import br.edu.fatec.grupog.repository.FotoRepository;
import br.edu.fatec.grupog.storage.GcsObjectService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    private final PetService petService;
    private final GcsObjectService gcsObjectService;

    public FotoService(FotoRepository fotoRepository, PetService petService, GcsObjectService gcsObjectService) {
        this.fotoRepository = fotoRepository;
        this.petService = petService;
        this.gcsObjectService = gcsObjectService;
    }

    public List<Foto> listarDoPet(Long petId) {
        return fotoRepository.findByPetIdOrderByDataFotoDesc(petId);
    }

    @Transactional
    public Foto enviar(Long petId, MultipartFile arquivo, LocalDate dataFoto, boolean principal) throws IOException {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new IllegalArgumentException("Selecione uma imagem.");
        }

        Pet pet = petService.buscar(petId);
        boolean primeiraFoto = fotoRepository.countByPetId(petId) == 0;
        boolean marcarPrincipal = principal || primeiraFoto;

        if (marcarPrincipal) {
            fotoRepository.limparPrincipal(petId);
        }

        String objectName = "pets/" + petId + "/" + UUID.randomUUID() + extensao(arquivo.getOriginalFilename());
        gcsObjectService.upload(arquivo.getInputStream(), objectName, arquivo.getContentType());

        Foto foto = new Foto();
        foto.setPet(pet);
        foto.setObjectName(objectName);
        foto.setDataFoto(dataFoto != null ? dataFoto : LocalDate.now());
        foto.setPrincipal(marcarPrincipal);
        return fotoRepository.save(foto);
    }

    /**
     * Apaga a foto no bucket e no banco local.
     * Se era a principal, a foto mais recente que sobrar vira a nova principal.
     */
    @Transactional
    public void apagar(Long petId, Long fotoId) {
        Foto foto = buscarDoPet(petId, fotoId);

        try {
            gcsObjectService.apagar(foto.getObjectName());
        } catch (IllegalArgumentException e) {
            // objeto ja nao existe no bucket: segue para limpar o registro no banco
        }
        fotoRepository.delete(foto);

        if (foto.isPrincipal()) {
            fotoRepository.findByPetIdOrderByDataFotoDesc(petId).stream()
                    .filter(f -> !f.getId().equals(fotoId))
                    .findFirst()
                    .ifPresent(f -> f.setPrincipal(true));
        }
    }

    /**
     * Marca a foto como principal (capa do catalogo), desmarcando as outras do pet.
     */
    @Transactional
    public void tornarPrincipal(Long petId, Long fotoId) {
        if (buscarDoPet(petId, fotoId).isPrincipal()) {
            return;
        }
        fotoRepository.limparPrincipal(petId);
        // busca de novo: o update em massa nao atualiza a entidade ja carregada
        buscarDoPet(petId, fotoId).setPrincipal(true);
    }

    /**
     * Download pela aplicacao (API do GCS, nao so o link publico).
     */
    public void download(Long petId, Long fotoId, HttpServletResponse response) throws IOException {
        Foto foto = buscarDoPet(petId, fotoId);
        String objectName = foto.getObjectName();
        String nomeArquivo = objectName.substring(objectName.lastIndexOf('/') + 1);

        Path temporario = Files.createTempFile("foto-", nomeArquivo);
        try {
            gcsObjectService.download(objectName, temporario);

            String contentType = Files.probeContentType(temporario);
            response.setContentType(contentType != null ? contentType : "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"");
            response.setContentLengthLong(Files.size(temporario));
            Files.copy(temporario, response.getOutputStream());
        } finally {
            Files.deleteIfExists(temporario);
        }
    }

    private Foto buscarDoPet(Long petId, Long fotoId) {
        return fotoRepository.findById(fotoId)
                .filter(f -> f.getPet().getId().equals(petId))
                .orElseThrow(() -> new IllegalArgumentException("Foto nao encontrada: " + fotoId));
    }

    private String extensao(String nomeOriginal) {
        if (nomeOriginal == null || !nomeOriginal.contains(".")) {
            return "";
        }
        return nomeOriginal.substring(nomeOriginal.lastIndexOf('.')).toLowerCase();
    }
}
