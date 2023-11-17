package databullet.infrastructure;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonMapper {

    private ObjectMapper objectMapper;

    public JsonMapper() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @SneakyThrows
    public String mapObjectToJson(Object obj) {
        return objectMapper.writeValueAsString(obj);
    }

    @SneakyThrows
    public <T> T mapJsonToObject(String json, Class<T> clazz) {
        if ("".equals(json)) {
            return clazz.getConstructor().newInstance();
        }
        return objectMapper.readValue(json, clazz);
    }

    @SneakyThrows
    public <T> T mapJsonToObject(Path jsonPath, Class<T> clazz) {
        return objectMapper.readValue(Files.readString(jsonPath, Charset.defaultCharset()), clazz);
    }
}

