package databullet.domain.definition.dataspec.types;

import databullet.domain.definition.dataspec.DataSpecType;
import lombok.Data;

@Data
public class SequenceType extends DataSpecType {

  private Integer start;
  private Integer increment;
  private Integer current;

}