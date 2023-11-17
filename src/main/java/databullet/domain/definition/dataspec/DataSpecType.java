package databullet.domain.definition.dataspec;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import databullet.domain.definition.dataspec.options.Options;
import lombok.Data;

@JsonDeserialize(using = DataSpecTypeDeserializer.class)
@Data
public class DataSpecType {

    private String name;
    private Options options;
}

