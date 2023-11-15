package databullet.domain.definition.data.type;

import com.fasterxml.jackson.annotation.JsonTypeName;
import databullet.domain.definition.data.type.options.Options;
import databullet.infrastructure.JsonMapper;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

public class DataTypeFactory {

    private static final JsonMapper mapper = new JsonMapper();

    private static HashMap<String, Function<String, Options>> optionsMap;

    @SneakyThrows
    public static DataType create(String name, String options) {

        DataType dataType = new DataType();
        dataType.setName(name);
        if (getOptionsMap().containsKey(name)) {
            dataType.setOptions(getOptionsMap().get(name).apply(options));
        }

        return dataType;
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
