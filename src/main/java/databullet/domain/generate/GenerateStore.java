package databullet.domain.generate;

import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.generate.GenerateTable;
import lombok.Getter;

import java.util.*;
import java.util.function.Supplier;

public class GenerateStore {

    @Getter
    private final int batchSize;

    private Set<String> initializedTables = new HashSet<>();

    private Set<String> finishedTables = new HashSet<>();

    @Getter
    private ThreadLocal<GenerateTable> processingTables = new InheritableThreadLocal<>();

    @Getter
    private ThreadLocal<GenerateColumn> processingColumns = new InheritableThreadLocal<>();

    @Getter
    private ThreadLocal<Integer> processingRowIndex = new InheritableThreadLocal<>();

    private Map<GenerateColumn, List<Object>> relationColumnValuesMap = new HashMap<>();

    public GenerateStore(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setCurrentProcessing(GenerateColumn column, Integer rowCount) {
        this.processingTables.set(column.getOwnerTable());
        this.processingColumns.set(column);
        this.processingRowIndex.set(rowCount);
    }

    public void initialize(GenerateTable table) {
        initializedTables.add(table.getName());
    }

    public void finish(GenerateTable table) {
        finishedTables.add(table.getName());
    }

    public boolean isInitialized(GenerateTable table) {
        return initializedTables.contains(table.getName());
    }

    public boolean isFinished(GenerateTable table) {
        return finishedTables.contains(table.getName());
    }

    public void registerColumnValue(GenerateColumn column, Object value) {
        relationColumnValuesMap.putIfAbsent(column, new ArrayList<>());
        relationColumnValuesMap.get(column).add(value);
    }

    public boolean existsColumnValue(GenerateColumn column, Integer index) {
        if (!relationColumnValuesMap.containsKey(column)) {
            return false;
        }
        return relationColumnValuesMap.get(column).size() > index;
    }

    public Object getColumnValueOrDefault(GenerateColumn column, Integer index, Supplier<Object> supplier) {
        if (!relationColumnValuesMap.containsKey(column)) {
            return supplier.get();
        }
        List<Object> values = relationColumnValuesMap.get(column);
        return values.get(index % (values.size()));
    }

    public Object getColumnValue(GenerateColumn column, Integer index) {
        List<Object> values = relationColumnValuesMap.get(column);
        return values.get(index % (values.size()));
    }

    public void setRelationValueRecursiveChildren(GenerateColumn column, Object value) {
        for (GenerateColumn child : column.getRelationChildren()) {
            registerColumnValue(child, value);
            setRelationValueRecursiveChildren(child, value);
        }
    }
}

