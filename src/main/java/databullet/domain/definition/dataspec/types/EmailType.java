package databullet.domain.definition.dataspec.types;

import databullet.domain.definition.dataspec.DataSpecType;
import lombok.Data;

@Data
public class EmailType extends DataSpecType {

  private Integer length;

  public EmailType() {
    this.length = 20;
  }
}
