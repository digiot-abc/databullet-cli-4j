package databullet.domain.generate;

import databullet.domain.definition.data.type.options.SequenceOptions;

public class SequenceGenerator implements Generator<Integer, SequenceOptions> {

    Integer current = null;

    @Override
    public Integer generate(SequenceOptions sequenceOptions) {

        if (current == null) {
            current = sequenceOptions.getStart();
        }

        return null;
    }

    @Override
    public void initialize() {

    }
}
