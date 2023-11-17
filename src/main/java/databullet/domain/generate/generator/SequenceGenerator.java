package databullet.domain.generate.generator;

import databullet.domain.definition.dataspec.options.SequenceOptions;

@GenerateOptions(SequenceOptions.class)
public class SequenceGenerator extends Generator<Integer, SequenceOptions> {

    Integer current = null;

    public SequenceGenerator(SequenceOptions sequenceOptions) {
        super(sequenceOptions);
    }

    @Override
    public Integer generate() {

        if (current != null) {
            current += options.getIncrement();
        } else {
            current = options.getStart();
        }

        return current;
    }
}
