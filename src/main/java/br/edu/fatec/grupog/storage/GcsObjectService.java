package br.edu.fatec.grupog.storage;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Operacoes sobre objetos do bucket.
 * Upload e listagem ja funcionam. Download e exclusao ficam para o grupo.
 */
public class GcsObjectService {

    private final Storage storage;
    private final String bucketName;

    public GcsObjectService(Storage storage, String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }

    /**
     * Envia um arquivo local para o bucket.
     * O nome no bucket (objectName) e o "caminho" do arquivo na nuvem.
     */
    public void upload(Path arquivoLocal, String objectName) throws IOException {
        if (!Files.isRegularFile(arquivoLocal)) {
            throw new IOException("Arquivo local nao encontrado: " + arquivoLocal);
        }

        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        storage.createFrom(blobInfo, arquivoLocal);

        System.out.println("Upload ok: " + objectName);
        System.out.println("URL publica: " + GcsConfig.PUBLIC_BASE_URL + objectName);
    }

    /**
     * Lista os objetos que ja estao no bucket.
     */
    public void listar() {
        Page<Blob> blobs = storage.list(bucketName);
        boolean vazio = true;

        for (Blob blob : blobs.iterateAll()) {
            vazio = false;
            System.out.printf("- %s (%d bytes)%n", blob.getName(), blob.getSize());
        }

        if (vazio) {
            System.out.println("Bucket vazio.");
        }
    }

    /**
     * TODO (grupo): baixar um objeto do bucket para um arquivo no computador.
     *
     * Dicas da documentacao:
     * - Blob blob = storage.get(bucketName, objectName);
     * - se blob for null, o objeto nao existe
     * - blob.downloadTo(destino);
     */
    public void download(String objectName, Path destino) {
        Blob blob = storage.get(bucketName, objectName);
        if (blob == null) {
            System.out.println("Objeto nao encontrado: " + objectName);
            return;
        }

        blob.downloadTo(destino);
        System.out.println("Download ok: " + destino);
    }

    /**
     * TODO (grupo): apagar um objeto do bucket.
     *
     * Dica da documentacao:
     * - boolean apagou = storage.delete(bucketName, objectName);
     * - tratar o caso em que o objeto nao existe (apagou == false)
     */
    public void apagar(String objectName) {
        boolean apagou = storage.delete(bucketName, objectName);
        if (!apagou) {
            System.out.println("Objeto nao encontrado: " + objectName);
            return;
        }

        System.out.println("Objeto apagado: " + objectName);
    }
}
