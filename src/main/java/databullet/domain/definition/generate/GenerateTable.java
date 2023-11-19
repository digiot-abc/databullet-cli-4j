package databullet.domain.definition.generate;

import databullet.domain.definition.dataspec.DataSpecTable;
import databullet.domain.definition.table.Table;
import lombok.Data;

import java.util.*;

@Data
public class GenerateTable implements Comparable<GenerateTable> {

  private GenerateTable parentTable;

  private Collection<GenerateTable> childTables = new TreeSet<>();

  private List<GenerateColumn> columns;

  private String name;

  private Integer rowCount;

  private Integer columnCount;

  private Integer relationSinkLevel = 0;

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

  @Override
  public int compareTo(GenerateTable o) {

    // 深さで比較
    int depthComparison = Integer.compare(this.relationSinkLevel, o.relationSinkLevel);
    if (depthComparison != 0) {
      return depthComparison;
    }

    // 名前で比較
    int nameComparison = this.name.compareTo(o.name);
    if (nameComparison != 0) {
      return nameComparison;
    }

    // 列数で比較
    return Integer.compare(this.columnCount, o.columnCount);
  }
}
