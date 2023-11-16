package databullet.domain.definition.data.options;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonTypeName("date")
@Data
@AllArgsConstructor
@JsonDeserialize(using = DateOptions.DateDeserializer.class)
public class DateOptions implements Options {

    private LocalDate start;

    private LocalDate end;

    public DateOptions() {
        end = LocalDate.now();
        start = end.minusYears(3);
        formatter = DateTimeFormatter.ISO_DATE;
    }

    @JsonIgnore
    private DateTimeFormatter formatter;

    public void setFormat(String format) {
        this.formatter = DateTimeFormatter.ofPattern(format);
    }

    public void setStart(String start) {
        this.start = LocalDate.parse(start, formatter);
    }

    public void setEnd(String end) {
        this.end = LocalDate.parse(end, formatter);
    }

    public static class DateDeserializer extends JsonDeserializer<DateOptions> {

        @Override
        public DateOptions deserialize(JsonParser parser, DeserializationContext context) throws IOException {

            JsonNode node = parser.readValueAsTree();
            DateOptions options = new DateOptions();

            if (node instanceof ObjectNode) {
                ObjectNode objectNode = (ObjectNode) node;
                if (objectNode.has("format")) {
                    options.setFormat(objectNode.get("format").asText());
                }
                if (objectNode.has("start")) {
                    options.setStart(objectNode.get("start").asText());
                }
                if (objectNode.has("end")) {
                    options.setEnd(objectNode.get("end").asText());
                }
            }

            return options;
        }
    }
}

