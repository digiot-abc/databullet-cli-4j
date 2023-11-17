package databullet.domain.definition.dataspec;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class DataSpecTypeDeserializer extends JsonDeserializer<DataSpecType> {

    @Override
    public DataSpecType deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode node = parser.readValueAsTree();

        String name = "";
        Object options = "";

        if (node instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) node;
            if (objectNode.has("name")) {
                name = objectNode.get("name").asText();
            }
            if (objectNode.has("options")) {
                options = objectNode.get("options");
            }
        }

        return DataSpecTypeFactory.create(name, options.toString());
    }
}
