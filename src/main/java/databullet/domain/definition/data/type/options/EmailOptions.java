package databullet.domain.definition.data.type.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("email")
@Data
public class EmailOptions implements Options {
}
