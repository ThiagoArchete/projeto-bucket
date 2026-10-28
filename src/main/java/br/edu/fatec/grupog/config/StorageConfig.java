package br.edu.fatec.grupog.config;

import br.edu.fatec.grupog.storage.GcsClientFactory;
import com.google.cloud.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class StorageConfig {

    @Bean
    public Storage storage() throws IOException {
        return GcsClientFactory.create();
    }
}
