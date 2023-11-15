package databullet.domain.definition.data.type.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("sequence")
@Data
public class SequenceOptions implements Options<Integer> {

    private Integer start;
    private Integer increment;
    private Integer current;

    public SequenceOptions() {
        initialize();
    }

    @Override
    public void initialize() {
        this.current = start;
    }

    @Override
    public Integer generate() {
        if (current == null) {
            current = start;
        }

        int nextValue = current;
        current += increment;
        return nextValue;
    }
}