package databullet.domain.definition.data.type.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.List;

@JsonTypeName("decimal")
@Data
public class DecimalOptions implements Options {

    private Integer min;
    private Integer max;

    public void setRange(Object range) {
        if (range instanceof List) {
            List<Integer> list = (List<Integer>) range;
            if (list.size() != 2) {
                throw new NumberFormatException();
            }
            min = list.get(0);
            max = list.get(1);
        }
    }
}