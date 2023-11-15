package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.DecimalOptions;

import java.util.Random;

@GenerateOptions(DecimalOptions.class)
public class DecimalGenerator implements Generator<Double> {

  private final long scaleFactor;
  private final long range;
  private final Random random = new Random();

  public DecimalGenerator(DecimalOptions options) {
    int digit = options.getDigit();
    int afterPointDigit = options.getAfterPointDigit();

    this.scaleFactor = (long) Math.pow(10, afterPointDigit);
    this.range = (long) Math.pow(10, digit) - scaleFactor;
  }

  @Override
  public Double generate() {
    long randomLong = (long) (random.nextDouble() * range);
    return (double) (randomLong + scaleFactor) / scaleFactor;
  }
}
