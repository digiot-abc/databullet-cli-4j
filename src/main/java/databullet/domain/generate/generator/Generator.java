package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.Options;

public abstract class Generator<T, U extends Options> {

    protected final U options;

    public Generator(U options) {
        this.options = options;
    }

    public abstract T generate();

    public void initialize() {
        // NOP
    }
}