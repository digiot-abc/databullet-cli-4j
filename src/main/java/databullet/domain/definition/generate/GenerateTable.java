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
    if (this.parentTable == null && o.parentTable != null) {
      return -1; // thisがparentTableがnullで、oがnullでない場合、thisを前に持ってくる
    }
    if (this.parentTable != null && o.parentTable == null) {
      return 1; // thisがnullでなく、oがparentTableがnullの場合、thisを後に持ってくる
    }

    // 両方のparentTableがnullか、両方がnullでない場合、nameを基準に比較
    int nameComparison = this.name.compareTo(o.name);
    if (nameComparison != 0) {
      return nameComparison;
    }

    // nameが同じ場合、列数で比較
    return Integer.compare(this.columnCount, o.columnCount);
  }

}
