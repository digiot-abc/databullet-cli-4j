package databullet.domain.definition.data.type.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.concurrent.ThreadLocalRandom;

@JsonTypeName("string")
@Data
public class StringOptions implements Options<String> {

    private Integer length;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public void setLength(Integer length) {
        if (length == null) {
            this.length = 0;
        } else {
            this.length = length;
        }
    }

    @Override
    public String generate() {

        if (length <= 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder(length);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            builder.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return builder.toString();
    }
}
