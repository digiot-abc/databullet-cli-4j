package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.ReferenceType;

// TODO
@GenerateOptions(ReferenceType.class)
public class ReferenceGenerator extends Generator<String, ReferenceType> {

    public ReferenceGenerator(ReferenceType referenceType) {
        super(referenceType);
    }

    @Override
    public String generate() {
        return "ref";
    }
}
