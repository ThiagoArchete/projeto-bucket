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

## Funcionalidades

- Cadastro do pet (nome, especie/raca, responsavel, nascimento)
- Upload de n fotos para o bucket, com data e marcacao de foto principal
- Trocar a foto principal pelo botao **Principal** no detalhe
- Catalogo com miniatura da foto principal (URL publica do bucket)
- Detalhe do pet com historico fotografico por data
- Download da foto pela API do GCS (botao **Baixar**)
- Apagar foto: remove o objeto do bucket e o registro no banco; se era a principal, outra foto assume
- Listagem dos objetos no bucket (`/bucket`)

## Demonstracao

O enunciado pede **pelo menos 10 pets** e **3 imagens por pet**. Os dados ficam no banco local
(`data/`, fora do Git); as imagens ficam no bucket em `pets/<id-do-pet>/`.

Na primeira execucao com o banco vazio (ex.: outro computador), `DadosIniciais.java` cadastra
automaticamente os 10 pets da demonstracao, apontando para as 30 imagens que ja estao no bucket.
Se o banco ja tiver pets, nada e feito.

Para apresentar em outro computador:

1. Instalar Java 17+ e Maven
2. Clonar o repositorio e colocar `sa-grupo-g-key.json` na raiz
3. `mvn spring-boot:run` e abrir http://localhost:8080

Nao apague pela aplicacao as fotos da demonstracao antes de apresentar: o arquivo sai do bucket
e o cadastro automatico continuaria apontando para ele.

Console H2: http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:file:./data/petshop`
