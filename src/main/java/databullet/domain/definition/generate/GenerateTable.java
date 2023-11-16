package databullet.domain.definition.generate;

import databullet.domain.definition.data.DataSpecTable;
import databullet.domain.definition.table.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class GenerateTable {

  private GenerateTable parentTable;

  private List<GenerateTable> childTables = new ArrayList<>();

  private List<GenerateColumn> columns;

  private String name;

  private Integer rowCount;

  private Integer columnCount;

  private Table table;

  private DataSpecTable dataSpecTable;

  public GenerateTable(Table table, DataSpecTable dataSpecTable) {
    this.name = table.getName();
    this.table = table;
    this.dataSpecTable = dataSpecTable;
    this.columnCount = table.getColumnCount();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GenerateTable that = (GenerateTable) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }
}
