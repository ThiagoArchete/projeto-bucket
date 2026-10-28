package br.edu.fatec.grupog.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String especieRaca;
    private String responsavel;
    private LocalDate dataNascimento;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    private List<Foto> fotos = new ArrayList<>();

    public Foto getFotoPrincipal() {
        return fotos.stream()
                .filter(Foto::isPrincipal)
                .findFirst()
                .orElse(fotos.isEmpty() ? null : fotos.get(0));
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecieRaca() {
        return especieRaca;
    }

    public void setEspecieRaca(String especieRaca) {
        this.especieRaca = especieRaca;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Foto> getFotos() {
        return fotos;
    }
}
