package br.edu.fatec.grupog.service;

import br.edu.fatec.grupog.model.Pet;
import br.edu.fatec.grupog.repository.PetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
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
}
