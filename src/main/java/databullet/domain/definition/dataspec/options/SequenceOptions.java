package databullet.domain.definition.dataspec.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("sequence")
@Data
public class SequenceOptions implements Options {

    private Integer start;
    private Integer increment;
    private Integer current;
}