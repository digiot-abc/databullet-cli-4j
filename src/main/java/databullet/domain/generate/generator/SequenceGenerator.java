package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.SequenceOptions;

@GenerateOptions(SequenceOptions.class)
public class SequenceGenerator implements Generator<Integer> {

    Integer current = null;

    private SequenceOptions sequenceOptions;

    public SequenceGenerator(SequenceOptions sequenceOptions) {
        this.sequenceOptions = sequenceOptions;
    }

    @Override
    public Integer generate() {

        if (current == null) {
            current = sequenceOptions.getStart();
        }

        current += sequenceOptions.getIncrement();

        return current;
    }

    @Override
    public void initialize() {

    }
}
