package databullet.domain.generate;

public interface Generator<T> {

    T generate();

    default void initialize() {
        // NOP
    }
}