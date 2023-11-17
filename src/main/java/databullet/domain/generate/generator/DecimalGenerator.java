package databullet.domain.generate.generator;

import databullet.domain.definition.dataspec.options.DecimalOptions;

import java.util.Random;

@GenerateOptions(DecimalOptions.class)
public class DecimalGenerator extends Generator<Double, DecimalOptions> {

  private final long scaleFactor;
  private final long range;
  private final Random random = new Random();

  public DecimalGenerator(DecimalOptions options) {
    super(options);
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
