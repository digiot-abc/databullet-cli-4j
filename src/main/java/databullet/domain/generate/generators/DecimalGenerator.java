package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.DecimalType;
import databullet.domain.generate.GenerateOptions;
import databullet.domain.generate.GenerateStore;

import java.util.Random;

@GenerateOptions(DecimalType.class)
public class DecimalGenerator extends Generator<Double, DecimalType> {

  private final long scaleFactor;
  private final long range;
  private final Random random = new Random();

  public DecimalGenerator(DecimalType options) {
    super(options);
    int digit = options.getDigit();
    int afterPointDigit = options.getAfterPointDigit();

    this.scaleFactor = (long) Math.pow(10, afterPointDigit);
    this.range = (long) Math.pow(10, digit) - scaleFactor;
  }

  @Override
  public Double generate(GenerateStore store) {
    long randomLong = (long) (random.nextDouble() * range);
    return (double) (randomLong + scaleFactor) / scaleFactor;
  }
}
