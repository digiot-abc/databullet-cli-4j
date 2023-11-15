package databullet.domain.generate.generator;

public interface Generator<T> {

    T generate();

    default void initialize() {
        // NOP
    }
}