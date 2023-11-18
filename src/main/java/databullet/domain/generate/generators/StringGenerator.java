package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.StringType;

import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(StringType.class)
public class StringGenerator extends Generator<String, StringType> {

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public StringGenerator(StringType stringType) {
    super(stringType);
  }

  @Override
  public String generate() {

    int length = type.getLength();

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
