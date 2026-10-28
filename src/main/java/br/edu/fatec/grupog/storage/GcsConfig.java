package br.edu.fatec.grupog.storage;

/**
 * Dados do bucket do grupo-g (aula de Object Storage / GCS).
 */
public final class GcsConfig {

    public static final String PROJECT_ID = "serjava-demo";
    public static final String BUCKET_NAME = "fatec-2026-grupo-g";
    public static final String CREDENTIALS_FILE = "sa-grupo-g-key.json";
    public static final String PUBLIC_BASE_URL = "https://storage.googleapis.com/fatec-2026-grupo-g/";

    private GcsConfig() {
    }
}
