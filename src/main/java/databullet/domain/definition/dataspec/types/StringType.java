package databullet.domain.definition.dataspec.types;

import databullet.domain.definition.dataspec.DataSpecType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StringType extends DataSpecType {

  private Integer length;

  public StringType(Integer length) {
    setLength(length);
  }

  public void setLength(Integer length) {
    if (length == null) {
      this.length = 0;
    } else if (length > 1000) {
      this.length = 1000;
    } else {
      this.length = length;
    }
  }
}
