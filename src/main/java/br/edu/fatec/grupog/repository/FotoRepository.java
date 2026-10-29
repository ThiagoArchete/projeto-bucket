package br.edu.fatec.grupog.repository;

import br.edu.fatec.grupog.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {

    List<Foto> findByPetIdOrderByDataFotoDesc(Long petId);

    long countByPetId(Long petId);

    @Modifying
    @Query("update Foto f set f.principal = false where f.pet.id = :petId")
    void limparPrincipal(@Param("petId") Long petId);
}
