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
     * TODO (outro membro do grupo): apagar a foto no bucket E no banco local.
     *
     * Passos sugeridos:
     * 1. Buscar a Foto pelo id; conferir se pertence ao petId.
     * 2. Chamar gcsObjectService.apagar(foto.getObjectName()).
     * 3. fotoRepository.delete(foto).
     * 4. Se a foto era principal e o pet ainda tem outras fotos,
     *    marcar uma delas como principal.
     */
    @Transactional
    public void apagar(Long petId, Long fotoId) {
        throw new UnsupportedOperationException("TODO: implementar exclusao da imagem (bucket + banco)");
    }

    /**
     * TODO (outro membro do grupo): download pela aplicacao (API do GCS, nao so o link publico).
     *
     * Passos sugeridos:
     * 1. Buscar a Foto pelo id.
     * 2. Definir headers no response (Content-Disposition: attachment).
     * 3. Baixar com gcsObjectService.download(...) para um arquivo temporario
     *    e copiar para response.getOutputStream(), ou equivalente.
     */
    public void download(Long petId, Long fotoId, HttpServletResponse response) {
        throw new UnsupportedOperationException("TODO: implementar download pela aplicacao");
    }

    private String extensao(String nomeOriginal) {
        if (nomeOriginal == null || !nomeOriginal.contains(".")) {
            return "";
        }
        return nomeOriginal.substring(nomeOriginal.lastIndexOf('.')).toLowerCase();
    }
}
