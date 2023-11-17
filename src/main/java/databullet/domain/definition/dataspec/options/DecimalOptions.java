package databullet.domain.definition.dataspec.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeName("decimal")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecimalOptions implements Options {

  private Integer digit;
  private Integer afterPointDigit;
}
