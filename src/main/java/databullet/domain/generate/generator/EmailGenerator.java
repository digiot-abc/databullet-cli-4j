package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.EmailOptions;

@GenerateOptions(EmailOptions.class)
public class EmailGenerator implements Generator<String> {

    private EmailOptions emailOptions;

    public EmailGenerator(EmailOptions emailOptions) {
        this.emailOptions = emailOptions;
    }

    @Override
    public String generate() {
        return "test@mail.com";
    }
}
