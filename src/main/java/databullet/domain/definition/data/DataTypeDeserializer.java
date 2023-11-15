package databullet.domain.definition.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class DataTypeDeserializer extends JsonDeserializer<DataType> {

    @Override
    public DataType deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode node = parser.readValueAsTree();
        DataType dataType = new DataType();

        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;

            if (objectNode.has("name")) {
                dataType.setName(objectNode.get("name").asText());
            }

            if (objectNode.has("options")) {
                dataType.setOptions(objectNode.get("options"));
            }
        }

        return dataType;
    }
}
