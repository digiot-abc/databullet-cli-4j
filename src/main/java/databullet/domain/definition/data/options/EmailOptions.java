package databullet.domain.definition.data.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("email")
@Data
public class EmailOptions implements Options {

    private Integer length;

    public EmailOptions() {
        this.length = 20;
    }
}
