# PetShop (grupo-g)

Aplicacao Java (Spring Boot) do tema **petshop** da aula de Object Storage.

- Dados do pet: banco local H2
- Arquivos de imagem: bucket `fatec-2026-grupo-g`

## Como rodar

1. Java 17+ e Maven
2. `sa-grupo-g-key.json` na raiz do projeto (nao compartilhe a chave)
3. Na pasta do projeto:

```powershell
mvn spring-boot:run
```

Abra http://localhost:8080

## O que ja funciona

- Cadastro do pet (nome, especie/raca, responsavel, nascimento)
- Upload de n fotos para o bucket, com data e marcacao de foto principal
- Catalogo com miniatura da foto principal (URL publica do bucket)
- Detalhe do pet com historico fotografico por data
- Listagem dos objetos no bucket (`/bucket`)

## Tarefa do outro membro

Em `FotoService.java`:

1. `apagar` — remover a imagem do bucket e o registro no banco
2. `download` — baixar a imagem pela API do GCS (nao so o link publico)

Os botoes **Apagar** e **Baixar** ja existem na tela de detalhe.

## Demonstracao

O enunciado pede **pelo menos 10 pets** e **3 imagens por pet**. Cadastrem pela tela.

Console H2: http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:file:./data/petshop`
