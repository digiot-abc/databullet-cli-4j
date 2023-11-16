package databullet.domain.definition.generate;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.table.Column;
import lombok.Data;

import java.util.List;

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
}
