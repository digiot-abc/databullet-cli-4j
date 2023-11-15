package databullet.domain.definition.data.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("string")
@Data
public class StringOptions implements Options {

    private Integer length;

    public void setLength(Integer length) {
        if (length == null) {
            this.length = 0;
        } else {
            this.length = length;
        }
    }
}
