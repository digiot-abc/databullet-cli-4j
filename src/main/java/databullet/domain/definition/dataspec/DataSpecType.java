package databullet.domain.definition.dataspec;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import databullet.domain.definition.dataspec.types.*;
import lombok.Data;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "name")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ByteType.class, name = "byte"),
    @JsonSubTypes.Type(value = TimeType.class, name = "time"),
    @JsonSubTypes.Type(value = DateTimeType.class, name = "datetime"),
    @JsonSubTypes.Type(value = DateType.class, name = "date"),
    @JsonSubTypes.Type(value = DecimalType.class, name = "decimal"),
    @JsonSubTypes.Type(value = EmailType.class, name = "email"),
    @JsonSubTypes.Type(value = IntType.class, name = "int"),
    @JsonSubTypes.Type(value = PaddedNumberType.class, name = "padded_number"),
    @JsonSubTypes.Type(value = ReferenceType.class, name = "reference"),
    @JsonSubTypes.Type(value = SequenceType.class, name = "sequence"),
    @JsonSubTypes.Type(value = StringType.class, name = "string")
})
@Data
public class DataSpecType {

  private String name;
}

