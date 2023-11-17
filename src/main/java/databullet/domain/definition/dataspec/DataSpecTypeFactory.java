package databullet.domain.definition.dataspec;

import com.fasterxml.jackson.annotation.JsonTypeName;
import databullet.domain.definition.dataspec.options.Options;
import databullet.infrastructure.JsonMapper;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

public class DataSpecTypeFactory {

    private static final JsonMapper mapper = new JsonMapper();

    private static HashMap<String, Function<String, Options>> optionsMap;

    @SneakyThrows
    public static DataSpecType create(String name, String options) {

        DataSpecType dataSpecType = new DataSpecType();
        dataSpecType.setName(name);
        if (getOptionsMap().containsKey(name)) {
            dataSpecType.setOptions(getOptionsMap().get(name).apply(options));
        }

        return dataSpecType;
    }

    private static HashMap<String, Function<String, Options>> getOptionsMap() {

        if (optionsMap == null) {

            optionsMap = new HashMap<>();

            Reflections reflections = new Reflections(Options.class.getPackage().getName());
            Set<Class<? extends Options>> classes =
                    reflections.getSubTypesOf(Options.class);

            for (Class<? extends Options> clazz : classes) {
                String name = clazz.getAnnotation(JsonTypeName.class).value();
                optionsMap.put(name, o -> mapper.mapJsonToObject(o, clazz));
            }
        }

        return optionsMap;
    }
}
