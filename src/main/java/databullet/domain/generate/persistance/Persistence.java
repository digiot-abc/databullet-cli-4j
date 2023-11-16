package databullet.domain.generate.persistance;

import databullet.domain.definition.generate.GenerateTable;
import databullet.domain.generate.GenerateRecord;

import java.util.List;

public interface Persistence {

    void persist(GenerateTable table, List<GenerateRecord> records);
}
