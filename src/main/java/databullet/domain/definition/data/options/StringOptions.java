package databullet.domain.definition.data.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonTypeName("string")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
