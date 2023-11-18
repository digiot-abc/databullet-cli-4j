package databullet.domain.definition.dataspec.types;

import databullet.domain.definition.dataspec.DataSpecType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecimalType extends DataSpecType {

  private Integer digit;
  private Integer afterPointDigit;
}
