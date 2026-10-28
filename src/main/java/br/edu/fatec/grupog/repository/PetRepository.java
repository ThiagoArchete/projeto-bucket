package br.edu.fatec.grupog.repository;

import br.edu.fatec.grupog.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
