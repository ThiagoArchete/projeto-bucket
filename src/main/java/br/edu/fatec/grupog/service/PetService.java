package br.edu.fatec.grupog.service;

import br.edu.fatec.grupog.model.Foto;
import br.edu.fatec.grupog.model.Pet;
import br.edu.fatec.grupog.repository.PetRepository;
import br.edu.fatec.grupog.storage.GcsObjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final GcsObjectService gcsObjectService;

    public PetService(PetRepository petRepository, GcsObjectService gcsObjectService) {
        this.petRepository = petRepository;
        this.gcsObjectService = gcsObjectService;
    }

    public List<Pet> listar() {
        return petRepository.findAll();
    }

    public Pet buscar(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pet nao encontrado: " + id));
    }

    public Pet salvar(Pet pet) {
        return petRepository.save(pet);
    }

    /**
     * Exclui o pet: apaga as fotos dele no bucket e depois o registro no banco
     * (as fotos do banco saem junto pelo cascade).
     */
    @Transactional
    public void excluir(Long id) {
        Pet pet = buscar(id);
        for (Foto foto : pet.getFotos()) {
            try {
                gcsObjectService.apagar(foto.getObjectName());
            } catch (IllegalArgumentException e) {
                // objeto ja nao existe no bucket
            }
        }
        petRepository.delete(pet);
    }
}
