package br.edu.fatec.grupog.model;

import br.edu.fatec.grupog.storage.GcsConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

@Entity
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pet pet;

    private String objectName;
    private boolean principal;
    private LocalDate dataFoto;

    public String getUrlPublica() {
        return GcsConfig.PUBLIC_BASE_URL + objectName;
    }

    public Long getId() {
        return id;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public LocalDate getDataFoto() {
        return dataFoto;
    }

    public void setDataFoto(LocalDate dataFoto) {
        this.dataFoto = dataFoto;
    }
}
