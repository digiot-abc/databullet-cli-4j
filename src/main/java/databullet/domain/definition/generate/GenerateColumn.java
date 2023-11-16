package databullet.domain.definition.generate;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.table.Column;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class GenerateColumn {

  private GenerateColumn relationParent;

  private List<GenerateColumn> relationChildren;

  private GenerateTable ownerTable;

  private String name;

  private Column tableColumn;

  private DataSpecColumn dataSpecColumn;

  public GenerateColumn(Column tableColumn, DataSpecColumn dataSpecColumn) {
    this.name = tableColumn.getName();
    this.tableColumn = tableColumn;
    this.dataSpecColumn = dataSpecColumn;
  }

  @Override
  public int hashCode() {
    // relationParentとrelationChildrenを含まないハッシュコードの計算
    // ownerTableも循環参照を避けるために除外しています
    return Objects.hash(name, tableColumn.getName(), dataSpecColumn.getName());
  }
}
