package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.SequenceType;

@GenerateOptions(SequenceType.class)
public class SequenceGenerator extends Generator<Integer, SequenceType> {

    Integer current = null;

    public SequenceGenerator(SequenceType sequenceType) {
        super(sequenceType);
    }

    @Override
    public Integer generate() {

        if (current != null) {
            current += type.getIncrement();
        } else {
            current = type.getStart();
        }

        return current;
    }
}
