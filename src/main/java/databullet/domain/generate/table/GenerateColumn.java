package databullet.domain.generate.table;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.table.Column;
import lombok.Data;

@Data
public class GenerateColumn {

  private String name;

  private Column tableColumn;

  private DataSpecColumn dataSpecColumn;

  public GenerateColumn(Column tableColumn, DataSpecColumn dataSpecColumn) {
    this.name = tableColumn.getName();
    this.tableColumn = tableColumn;
    this.dataSpecColumn = dataSpecColumn;
  }
}
