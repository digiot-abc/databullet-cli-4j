package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.ReferenceOptions;

@GenerateOptions(ReferenceOptions.class)
public class ReferenceGenerator implements Generator<String> {

    private ReferenceOptions referenceOptions;

    public ReferenceGenerator(ReferenceOptions referenceOptions) {
        this.referenceOptions = referenceOptions;
    }

    @Override
    public String generate() {
        return "ref";
    }
}
