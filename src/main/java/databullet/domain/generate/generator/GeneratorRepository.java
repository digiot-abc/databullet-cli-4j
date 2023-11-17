package databullet.domain.generate.generator;

import databullet.domain.definition.generate.GenerateColumn;

import java.util.HashMap;
import java.util.Optional;

public class GeneratorRepository {

    private static final HashMap<GenerateColumn, Generator<?, ?>> generatorMap = new HashMap<>();

    private GeneratorRepository() {}

    public static Optional<Generator<?, ?>> findByColumn(GenerateColumn column) {
        return Optional.ofNullable(generatorMap.get(column));
    }

    public static void save(GenerateColumn column, Generator<?, ?> generator) {
        generatorMap.put(column, generator);
    }

}
