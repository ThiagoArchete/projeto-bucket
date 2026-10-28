package br.edu.fatec.grupog.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class GcsObjectService {

    private final Storage storage;

    public GcsObjectService(Storage storage) {
        this.storage = storage;
    }

    public void upload(InputStream arquivo, String objectName, String contentType) throws IOException {
        BlobId blobId = BlobId.of(GcsConfig.BUCKET_NAME, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType != null ? contentType : "application/octet-stream")
                .build();
        storage.createFrom(blobInfo, arquivo);
    }

    public List<Blob> listar() {
        List<Blob> objetos = new ArrayList<>();
        storage.list(GcsConfig.BUCKET_NAME).iterateAll().forEach(objetos::add);
        return objetos;
    }

    public void download(String objectName, Path destino) {
        Blob blob = storage.get(GcsConfig.BUCKET_NAME, objectName);
        if (blob == null) {
            throw new IllegalArgumentException("Objeto nao encontrado no bucket: " + objectName);
        }
        blob.downloadTo(destino);
    }

    public void apagar(String objectName) {
        boolean apagou = storage.delete(GcsConfig.BUCKET_NAME, objectName);
        if (!apagou) {
            throw new IllegalArgumentException("Objeto nao encontrado no bucket: " + objectName);
        }
    }
}
