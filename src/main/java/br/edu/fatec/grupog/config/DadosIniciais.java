package br.edu.fatec.grupog.config;

import br.edu.fatec.grupog.model.Foto;
import br.edu.fatec.grupog.model.Pet;
import br.edu.fatec.grupog.repository.PetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Cadastra os 10 pets da demonstracao quando o banco local esta vazio
 * (ex.: primeira execucao em outro computador).
 * As imagens ja estao no bucket; aqui so recriamos os registros que apontam para elas.
 */
@Component
public class DadosIniciais implements CommandLineRunner {

    private final PetRepository petRepository;

    public DadosIniciais(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public void run(String... args) {
        if (petRepository.count() > 0) {
            return;
        }

        Pet pet;

        pet = pet("Bob", "Shih Tzu", "Pedro", "2026-05-10");
        foto(pet, "pets/1/2a1731dc-3219-4758-81fc-72777fa0cb12.jpg", "2026-10-12", false);
        foto(pet, "pets/1/1428b4e9-f21e-4ab9-8ee4-2fb0da376e46.jpg", "2026-08-02", false);
        foto(pet, "pets/1/384315bf-a659-49bd-86cf-984ed4300bc0.jpg", "2026-05-20", true);
        petRepository.save(pet);

        pet = pet("Totó do Claudião", "Cachorro · Vira-lata", "Claudião", "2019-03-14");
        foto(pet, "pets/3/bd181bce-1ff0-46a5-9cac-4063d0b402ab.jpg", "2026-09-03", false);
        foto(pet, "pets/3/d50aba39-6f1f-47c2-baa6-3a8ff7f8770c.jpg", "2025-07-10", false);
        foto(pet, "pets/3/18f5f19f-99af-4652-8e52-548b3cef5e65.jpg", "2024-12-25", true);
        petRepository.save(pet);

        pet = pet("Thor", "Cachorro · Husky Siberiano", "Mariana Souza", "2021-07-02");
        foto(pet, "pets/4/bf7c0817-8fda-4c0f-a0c2-7187be555249.jpg", "2026-01-20", false);
        foto(pet, "pets/4/7b1a78b1-1976-478a-a7bf-782bc4dac3af.jpg", "2025-06-15", true);
        foto(pet, "pets/4/be7f945f-40e9-465a-8806-86a17ffc69e4.jpg", "2025-06-15", false);
        petRepository.save(pet);

        pet = pet("Mel", "Cachorro · Golden Retriever", "Rafael Lima", "2020-11-23");
        foto(pet, "pets/5/7f2d34ed-15c9-4487-8065-e30dc1f9ed76.jpg", "2026-08-17", false);
        foto(pet, "pets/5/d388434f-bc63-4e7b-a35e-8d76614a3323.jpg", "2025-11-30", false);
        foto(pet, "pets/5/f58f7877-0362-4cbd-9121-164759c317be.jpg", "2025-02-08", true);
        petRepository.save(pet);

        pet = pet("Paçoca", "Cachorro · Beagle", "Juliana Alves", "2022-04-09");
        foto(pet, "pets/6/057911af-1d9c-4a91-8b0e-e3f620c75a23.jpg", "2026-04-09", false);
        foto(pet, "pets/6/8b03032b-587c-46e1-b0b3-f7aac1191628.jpg", "2026-04-09", false);
        foto(pet, "pets/6/d29a0eeb-d485-4b7e-a7cf-bdb0ae8e6b0a.jpg", "2025-04-09", true);
        petRepository.save(pet);

        pet = pet("Frida", "Cachorro · Pug", "Camila Rocha", "2023-01-17");
        foto(pet, "pets/7/1ae11c6d-3de4-4f67-830e-b0fb8d1af6eb.jpg", "2026-10-05", false);
        foto(pet, "pets/7/46b71e5c-621b-42ad-ab44-4ac48a8e2bf6.jpg", "2026-02-14", false);
        foto(pet, "pets/7/7daf2214-5e6a-4965-aa86-27bd348203ee.jpg", "2025-09-01", true);
        petRepository.save(pet);

        pet = pet("Salsicha", "Cachorro · Dachshund", "Lucas Ferreira", "2018-08-30");
        foto(pet, "pets/8/7260e5c6-c067-42e1-9ddd-0e59cbe55e74.jpg", "2026-06-21", false);
        foto(pet, "pets/8/b5cbbba7-8922-4896-8eef-cf8695fce5dd.jpg", "2025-08-30", false);
        foto(pet, "pets/8/2e7b3df9-cc9c-4c0a-aca7-ee7b8bb6335a.jpg", "2024-10-12", true);
        petRepository.save(pet);

        pet = pet("Luna", "Gato · Siamês", "Beatriz Costa", "2022-09-12");
        foto(pet, "pets/9/5e87255d-8243-443c-b5a6-290b6f8a78b3.jpg", "2026-07-07", false);
        foto(pet, "pets/9/bc20f7ab-dee6-48ef-81a4-0943888dcfba.jpg", "2025-12-24", false);
        foto(pet, "pets/9/0e05da3a-508c-49d9-ac02-1df396d1ace3.jpg", "2025-03-03", true);
        petRepository.save(pet);

        pet = pet("Nino", "Gato · Persa", "Gabriel Martins", "2021-02-05");
        foto(pet, "pets/10/3bd5fad4-f293-4c4a-b4c7-a4d2b6956d65.jpg", "2026-09-22", false);
        foto(pet, "pets/10/d263dd0b-5cac-4898-9fce-bfe7ce424261.png", "2025-10-31", false);
        foto(pet, "pets/10/7258c5e7-0970-49b7-9657-8a9e6225499f.jpg", "2025-05-18", true);
        petRepository.save(pet);

        pet = pet("Mingau", "Gato · Maine Coon", "Fernanda Ribeiro", "2020-06-28");
        foto(pet, "pets/11/2b285984-3c91-4190-9e71-569e1cf4a29b.jpg", "2026-10-01", false);
        foto(pet, "pets/11/6d7b8519-5dab-4d32-888c-fdc0bdda1fb1.jpg", "2025-09-09", false);
        foto(pet, "pets/11/a67055cf-fe57-449a-824a-5d478e79339d.jpg", "2024-11-11", true);
        petRepository.save(pet);

        System.out.println("Dados iniciais: " + petRepository.count() + " pets cadastrados.");
    }

    private static Pet pet(String nome, String especieRaca, String responsavel, String nascimento) {
        Pet pet = new Pet();
        pet.setNome(nome);
        pet.setEspecieRaca(especieRaca);
        pet.setResponsavel(responsavel);
        pet.setDataNascimento(LocalDate.parse(nascimento));
        return pet;
    }

    private static void foto(Pet pet, String objectName, String data, boolean principal) {
        Foto foto = new Foto();
        foto.setPet(pet);
        foto.setObjectName(objectName);
        foto.setDataFoto(LocalDate.parse(data));
        foto.setPrincipal(principal);
        pet.getFotos().add(foto);
    }
}
