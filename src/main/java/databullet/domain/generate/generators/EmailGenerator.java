package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.EmailType;
import databullet.domain.generate.GenerateOptions;
import databullet.domain.generate.GenerateStore;

import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(EmailType.class)
public class EmailGenerator extends Generator<String, EmailType> {

    private static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final String[] DOMAINS = {"example.net", "nobody.info", "nothing.tech", "nowhere.org"};

    private int totalLength;

    public EmailGenerator(EmailType emailType) {
        super(emailType);
        this.totalLength = emailType.getLength();
    }

    public String generate(GenerateStore store) {

        String domain = DOMAINS[ThreadLocalRandom.current().nextInt(DOMAINS.length)];

        // ドメインの長さを引いた分だけローカル部分を生成
        int localPartLength = totalLength - domain.length() - 1; // -1 for @ symbol
        if (localPartLength <= 0) {
            throw new IllegalArgumentException("Total length must be greater than domain length.");
        }

        char[] text = new char[localPartLength];
        for (int i = 0; i < localPartLength; i++) {
            text[i] = ALPHANUMERIC.charAt(ThreadLocalRandom.current().nextInt(ALPHANUMERIC.length()));
        }

        return new String(text) + "@" + domain;
    }
}
