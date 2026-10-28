package br.edu.fatec.grupog.storage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Cria o cliente do GCS autenticado com a chave da Service Account.
 */
public final class GcsClientFactory {

    private GcsClientFactory() {
    }

    public static Storage create() throws IOException {
        Path keyFile = Path.of(GcsConfig.CREDENTIALS_FILE);
        if (!Files.exists(keyFile)) {
            throw new IOException(
                    "Arquivo de credencial nao encontrado: " + keyFile.toAbsolutePath()
                            + ". Coloque sa-grupo-g-key.json na raiz do projeto.");
        }

        try (InputStream in = Files.newInputStream(keyFile)) {
            GoogleCredentials credentials = GoogleCredentials.fromStream(in);
            return StorageOptions.newBuilder()
                    .setProjectId(GcsConfig.PROJECT_ID)
                    .setCredentials(credentials)
                    .build()
                    .getService();
        }
    }
}
