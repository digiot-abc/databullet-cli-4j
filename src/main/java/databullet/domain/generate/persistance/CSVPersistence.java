package databullet.domain.generate.persistance;

import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.GenerateRecord;

import java.nio.file.Path;
import java.util.List;

public class CSVPersistence implements Persistence {

    private Path outputBasePath;

    public CSVPersistence(Path outputBasePath) {
        this.outputBasePath = outputBasePath;
    }

    @Override
    public void persist(GenerateTable table, List<GenerateRecord> records) {

    }
}
