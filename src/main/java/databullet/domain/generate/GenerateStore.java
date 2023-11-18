package databullet.domain.generate;

import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.generate.GenerateTable;

import java.util.*;

public class GenerateStore {

    Set<String> finishedTables = new HashSet<>();

    Map<GenerateColumn, Object> relationColumnValue = new HashMap<>();

    public void finish(GenerateTable table) {
        finishedTables.add(table.getName());
//        for (GenerateTable childTable : table.getChildTables()) {
//            finishedTables.add(childTable.getName());
//        }
    }

    public void def(GenerateTable table) {
        finishedTables.remove(table.getName());
    }

    public boolean isFinished(GenerateTable table) {
        return finishedTables.contains(table);
    }
}

