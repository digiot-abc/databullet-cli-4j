package databullet.domain.generate.table;

import databullet.domain.definition.data.DataSpecTable;
import databullet.domain.definition.table.Table;
import lombok.Data;

import java.util.List;

@Data
public class GenerateTable {

  private List<GenerateTable> children;

  private String name;

  private Table table;

  private DataSpecTable dataSpecTable;

  public GenerateTable(Table table, DataSpecTable dataSpecTable) {
    this.name = table.getName();
    this.table = table;
    this.dataSpecTable = dataSpecTable;
  }
}
