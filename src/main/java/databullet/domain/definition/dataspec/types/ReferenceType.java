package databullet.domain.definition.dataspec.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import databullet.domain.definition.dataspec.DataSpecType;
import lombok.Data;

@Data
public class ReferenceType extends DataSpecType {

  private String referencedTable;
  private String referencedColumn;

  @JsonIgnore
  public String getFQDNReferencedColumnName() {
    return String.join(".", referencedTable, referencedColumn);
  }
}
