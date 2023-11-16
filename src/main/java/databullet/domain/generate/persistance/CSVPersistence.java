package databullet.domain.generate.persistance;

import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.GenerateRecord;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class CSVPersistence implements Persistence {

    private Path outputBasePath;

    public CSVPersistence(Path outputBasePath) {
        this.outputBasePath = outputBasePath;
    }

    @SneakyThrows
    @Override
    public void persist(GenerateTable table, List<GenerateRecord> records) {
        Path filePath = outputBasePath.resolve(table.getName() + ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (GenerateRecord record : records) {
                String csvRecord = record.getData().values().stream()
                        .map(field -> "\"" + field + "\"")
                        .collect(Collectors.joining(","));
                writer.write(csvRecord);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
