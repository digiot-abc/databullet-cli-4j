package databullet.domain.definition.data.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeName("int")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntOptions implements Options {

  private Long min;
  private Long max;
}
