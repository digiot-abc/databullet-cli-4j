package databullet.domain.definition.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import databullet.domain.definition.data.options.Options;
import databullet.domain.definition.data.options.StringOptions;
import lombok.Data;

@JsonDeserialize(using = DataSpecTypeDeserializer.class)
@Data
public class DataSpecType {

    private String name;
    private Options options;

    public static DataSpecType empty() {

        StringOptions options = new StringOptions();
        options.setLength(0);

        DataSpecType dataSpecType = new DataSpecType();
        dataSpecType.setName("");
        dataSpecType.setOptions(options);

        return dataSpecType;
    }
}

