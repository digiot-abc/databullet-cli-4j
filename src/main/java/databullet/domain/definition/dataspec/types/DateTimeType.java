package databullet.domain.definition.dataspec.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databullet.domain.definition.dataspec.DataSpecType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@JsonDeserialize(using = DateTimeType.DateTimeDeserializer.class)
public class DateTimeType extends DataSpecType {

  private LocalDateTime start;

  private LocalDateTime end;
  @JsonIgnore
  private DateTimeFormatter formatter;

  public DateTimeType() {
    end = LocalDateTime.now();
    start = end.minusYears(3);
    formatter = DateTimeFormatter.ISO_DATE;
  }

  public void setFormat(String format) {
    this.formatter = DateTimeFormatter.ofPattern(format);
  }

  public void setStart(String start) {
    this.start = LocalDateTime.parse(start, formatter);
  }

  public void setEnd(String end) {
    this.end = LocalDateTime.parse(end, formatter);
  }

  public static class DateTimeDeserializer extends JsonDeserializer<DateTimeType> {

    @Override
    public DateTimeType deserialize(JsonParser parser, DeserializationContext context) throws IOException {

      JsonNode node = parser.readValueAsTree();
      DateTimeType options = new DateTimeType();

      if (node instanceof ObjectNode) {
        ObjectNode objectNode = (ObjectNode) node;
        if (objectNode.has("format")) {
          options.setEnd(objectNode.get("format").asText());
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

