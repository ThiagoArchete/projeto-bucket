package br.edu.fatec.grupog.storage;

import com.google.cloud.storage.Storage;

import java.nio.file.Path;
import java.util.Scanner;

/**
 * Menu de console para testar o bucket do grupo.
 */
public class App {

    public static void main(String[] args) {
        try {
            Storage storage = GcsClientFactory.create();
            GcsObjectService service = new GcsObjectService(storage, GcsConfig.BUCKET_NAME);
            executarMenu(service);
        } catch (Exception e) {
            System.err.println("Erro ao iniciar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void executarMenu(GcsObjectService service) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println();
            System.out.println("=== Bucket " + GcsConfig.BUCKET_NAME + " ===");
            System.out.println("1 - Enviar arquivo (upload)");
            System.out.println("2 - Listar arquivos");
            System.out.println("3 - Baixar arquivo (download)  <-- grupo");
            System.out.println("4 - Apagar arquivo             <-- grupo");
            System.out.println("0 - Sair");
            System.out.print("Opcao: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1" -> enviar(scanner, service);
                case "2" -> service.listar();
                case "3" -> baixar(scanner, service);
                case "4" -> apagar(scanner, service);
                case "0" -> continuar = false;
                default -> System.out.println("Opcao invalida.");
            }
        }
    }

    private static void enviar(Scanner scanner, GcsObjectService service) {
        try {
            System.out.print("Caminho do arquivo local: ");
            Path local = Path.of(scanner.nextLine().trim());

            System.out.print("Nome no bucket (ex: pasta/arquivo.txt): ");
            String objectName = scanner.nextLine().trim();

            service.upload(local, objectName);
        } catch (Exception e) {
            System.err.println("Falha no upload: " + e.getMessage());
        }
    }

    private static void baixar(Scanner scanner, GcsObjectService service) {
        System.out.print("Nome do objeto no bucket: ");
        String objectName = scanner.nextLine().trim();
        System.out.print("Caminho para salvar no computador: ");
        Path destino = Path.of(scanner.nextLine().trim());

        try {
            service.download(objectName, destino);
        } catch (UnsupportedOperationException e) {
            System.out.println("Ainda nao implementado. Completem GcsObjectService.download.");
        } catch (Exception e) {
            System.err.println("Falha no download: " + e.getMessage());
        }
    }

    private static void apagar(Scanner scanner, GcsObjectService service) {
        System.out.print("Nome do objeto no bucket: ");
        String objectName = scanner.nextLine().trim();

        try {
            service.apagar(objectName);
        } catch (UnsupportedOperationException e) {
            System.out.println("Ainda nao implementado. Completem GcsObjectService.apagar.");
        } catch (Exception e) {
            System.err.println("Falha ao apagar: " + e.getMessage());
        }
    }
}
