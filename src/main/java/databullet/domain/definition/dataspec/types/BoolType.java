package databullet.domain.definition.dataspec.types;

import databullet.domain.definition.dataspec.DataSpecType;
import lombok.Data;

@Data
public class BoolType extends DataSpecType {

  private Double trueRate = 1.0;

  public void setTrueRate(Double trueRate) {
    if (trueRate > 1.0) {
      this.trueRate = 1.0;
    } else if (trueRate < 0) {
      this.trueRate = 0.0;
    }
  }
}

