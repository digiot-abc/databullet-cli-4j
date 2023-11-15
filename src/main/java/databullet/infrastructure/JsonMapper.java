package databullet.infrastructure;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonMapper {

    private ObjectMapper objectMapper;

    public JsonMapper() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> T mapJsonToObject(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }

    public <T> T mapJsonToObject(Path jsonPath, Class<T> clazz) throws Exception {
        return objectMapper.readValue(Files.readString(jsonPath, Charset.defaultCharset()), clazz);
    }
}

