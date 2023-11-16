package databullet.domain.generate;

import databullet.domain.definition.generate.GenerateTable;

import java.util.*;

public class GenerateStore {

    Set<GenerateTable> finishedTables = new HashSet<>();

    private Map<GenerateTable, List<GenerateRecord>> recordsMap = new HashMap<>();

    public void addRecord(GenerateTable table, GenerateRecord record) {
        recordsMap.computeIfAbsent(table, t -> new ArrayList<>()).add(record);
    }

    public List<GenerateRecord> getRecords(GenerateTable table) {
        return recordsMap.computeIfAbsent(table, t -> new ArrayList<>());
    }

    public void finish(GenerateTable table) {
        finishedTables.add(table);
        for (GenerateTable childTable : table.getChildTables()) {
            finishedTables.add(childTable);
        }
    }

    public boolean isFinished(GenerateTable table) {
        return finishedTables.contains(table);
    }
}

