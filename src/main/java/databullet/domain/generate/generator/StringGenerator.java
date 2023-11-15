package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.StringOptions;

import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(StringOptions.class)
public class StringGenerator implements Generator<String> {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  StringOptions stringOptions;

  public StringGenerator(StringOptions stringOptions) {
    this.stringOptions = stringOptions;
  }

  @Override
  public String generate() {

    int length = stringOptions.getLength();

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
