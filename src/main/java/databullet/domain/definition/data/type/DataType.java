package databullet.domain.definition.data.type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import databullet.domain.definition.data.type.options.Options;
import databullet.domain.definition.data.type.options.StringOptions;
import lombok.Data;

@JsonDeserialize(using = DataTypeDeserializer.class)
@Data
public class DataType {

    private String name;
    private Options options;

    public static DataType empty() {

        StringOptions options = new StringOptions();
        options.setLength(0);

        DataType dataType = new DataType();
        dataType.setName("");
        dataType.setOptions(options);

        return dataType;
    }
}

