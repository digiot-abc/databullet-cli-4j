package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.IntType;

import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(IntType.class)
public class IntGenerator extends Generator<Integer, IntType> {

  private long min;
  private long max;
  private final ThreadLocalRandom random;

  public IntGenerator(IntType intType) {
    super(intType);
    this.min = intType.getMin();
    this.max = intType.getMax();
    this.random = ThreadLocalRandom.current();
  }

  @Override
  public Integer generate() {
    // (max - min + 1)を加算して、minとmaxの両方を含む範囲を確保
    return (int)(random.nextLong(max - min + 1) + min);
  }
}
