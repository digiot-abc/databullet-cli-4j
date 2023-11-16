package databullet.domain.generate;

import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.generate.GenerateTable;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

public class GenerateRecord {

  @Getter
  private Map<GenerateColumn, Object> data;

  public GenerateRecord() {
    data = new LinkedHashMap<>();
  }

  public void append(GenerateColumn column, Object obj) {
    data.put(column, obj);
  }

  @Override
  public String toString() {
    return data.toString();
  }

  public static GenerateRecord header(GenerateTable table) {
    GenerateRecord record = new GenerateRecord();
    for (GenerateColumn column : table.getColumns()) {
      record.append(column, column.getName());
    }
    return record;
  }
}
