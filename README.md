# Projeto Bucket (grupo-g)

Aplicacao Java de console para Object Storage no Google Cloud Storage.

- Bucket: `fatec-2026-grupo-g`
- Projeto GCP: `serjava-demo`

## Como rodar

1. Java 17+ e Maven instalados.
2. Coloque `sa-grupo-g-key.json` na raiz deste projeto (nao compartilhe essa chave).
3. No PowerShell, na pasta do projeto:

```powershell
mvn compile exec:java
```

## O que ja funciona

- Autenticacao com a Service Account
- Upload de arquivo
- Listagem dos objetos do bucket

Depois do upload, o arquivo fica publico em:

`https://storage.googleapis.com/fatec-2026-grupo-g/<nome-do-objeto>`

## O que o grupo precisa implementar

Em `GcsObjectService.java`:

- `download` — baixar um objeto para um arquivo local
- `apagar` — remover um objeto do bucket

Ha comentarios com dicas no codigo.
