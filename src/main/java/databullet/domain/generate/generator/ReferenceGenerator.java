package databullet.domain.generate.generator;

import databullet.domain.definition.dataspec.options.ReferenceOptions;

// TODO
@GenerateOptions(ReferenceOptions.class)
public class ReferenceGenerator extends Generator<String, ReferenceOptions> {

    public ReferenceGenerator(ReferenceOptions referenceOptions) {
        super(referenceOptions);
    }

    @Override
    public String generate() {
        return "ref";
    }
}
