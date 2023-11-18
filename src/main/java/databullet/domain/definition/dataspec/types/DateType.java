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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@JsonDeserialize(using = DateType.DateDeserializer.class)
public class DateType extends DataSpecType {

  private LocalDate start;

  private LocalDate end;
  @JsonIgnore
  private DateTimeFormatter formatter;

  public DateType() {
    end = LocalDate.now();
    start = end.minusYears(3);
    formatter = DateTimeFormatter.ISO_DATE;
  }

  public void setFormat(String format) {
    this.formatter = DateTimeFormatter.ofPattern(format);
  }

  public void setStart(String start) {
    this.start = LocalDate.parse(start, formatter);
  }

  public void setEnd(String end) {
    this.end = LocalDate.parse(end, formatter);
  }

  public static class DateDeserializer extends JsonDeserializer<DateType> {

    @Override
    public DateType deserialize(JsonParser parser, DeserializationContext context) throws IOException {

      JsonNode node = parser.readValueAsTree();
      DateType type = new DateType();

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

