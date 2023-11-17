package databullet.domain.definition.dataspec.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeName("pad_number")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaddedNumberOptions implements Options {

    private int totalLength;
    private int digits;
}
