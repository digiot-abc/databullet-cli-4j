package databullet.domain.generate.persistance;

import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.GenerateRecord;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PrintPersistence implements Persistence {

    @Override
    public void persist(GenerateTable table, List<GenerateRecord> records) {
        System.out.println(table.getName());
//        for (GenerateRecord record : records) {
//            String csvRecord = record.getData().values().stream()
//                    .map(value -> value == null ? "" : value.toString())
//                    .collect(Collectors.joining(","));
//            System.out.println(csvRecord);
//        }
    }
}
