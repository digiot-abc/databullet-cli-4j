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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@JsonDeserialize(using = TimeType.TimeDeserializer.class)
@Data
@AllArgsConstructor
public class TimeType extends DataSpecType {

  private LocalTime start;

  private LocalTime end;

  @JsonIgnore
  private DateTimeFormatter formatter;

  public TimeType() {
    end = LocalTime.now();
    start = LocalTime.MAX;
    formatter = DateTimeFormatter.ISO_TIME;
  }

  public void setFormat(String format) {
    this.formatter = DateTimeFormatter.ofPattern(format);
  }

  public void setStart(String start) {
    this.start = LocalTime.parse(start, formatter);
  }

  public void setEnd(String end) {
    this.end = LocalTime.parse(end, formatter);
  }

  public static class TimeDeserializer extends JsonDeserializer<TimeType> {

    @Override
    public TimeType deserialize(JsonParser parser, DeserializationContext context) throws IOException {

      JsonNode node = parser.readValueAsTree();
      TimeType type = new TimeType();

      if (node instanceof ObjectNode) {
        ObjectNode objectNode = (ObjectNode) node;
        if (objectNode.has("format")) {
          type.setFormat(objectNode.get("format").asText());
        }
        if (objectNode.has("start")) {
          type.setStart(objectNode.get("start").asText());
        }
        if (objectNode.has("end")) {
          type.setEnd(objectNode.get("end").asText());
        }
      }

      return type;
    }
  }
}
