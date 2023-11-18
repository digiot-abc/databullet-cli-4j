package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.SequenceType;
import databullet.domain.generate.GenerateOptions;
import databullet.domain.generate.GenerateStore;

@GenerateOptions(SequenceType.class)
public class SequenceGenerator extends Generator<Integer, SequenceType> {

    Integer current = null;

    public SequenceGenerator(SequenceType sequenceType) {
        super(sequenceType);
    }

    @Override
    public Integer generate(GenerateStore store) {

        if (current != null) {
            current += type.getIncrement();
        } else {
            current = type.getStart();
        }

        return current;
    }
}
