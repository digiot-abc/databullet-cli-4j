package databullet.domain.definition.data.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeName("numeric")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumericOptions implements Options {

  private Long min;
  private Long max;
}
