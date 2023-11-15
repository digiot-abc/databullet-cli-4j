package databullet.domain.definition.data.type.options;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@JsonTypeName("date")
@Data
public class DateOptions implements Options {

    @JsonIgnore
    private DateTimeFormatter formatter;

    public void setFormat(String format) {
        this.formatter = DateTimeFormatter.ofPattern(format);
    }

}