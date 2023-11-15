package databullet.domain.definition.data.type;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import databullet.domain.definition.data.type.DataType;
import databullet.domain.definition.data.type.DataTypeFactory;

import java.io.IOException;

public class DataTypeDeserializer extends JsonDeserializer<DataType> {

    @Override
    public DataType deserialize(JsonParser parser, DeserializationContext context) throws IOException {

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

        return new DataTypeFactory().create(name, options.toString());
    }
}
