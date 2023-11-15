package databullet.domain.generate;

import databullet.domain.definition.data.type.options.EmailOptions;

public class EmailGenerator implements Generator<String, EmailOptions> {

    @Override
    public String generate(EmailOptions emailOptions) {
        return "test@mail.com";
    }
}
