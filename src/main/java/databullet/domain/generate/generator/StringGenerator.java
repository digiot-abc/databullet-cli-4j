package databullet.domain.generate.generator;

import databullet.domain.definition.dataspec.options.StringOptions;

import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(StringOptions.class)
public class StringGenerator extends Generator<String, StringOptions> {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public StringGenerator(StringOptions stringOptions) {
    super(stringOptions);
  }

  @Override
  public String generate() {

    int length = options.getLength();

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
