package databullet.domain.generate.generator;

import databullet.domain.definition.dataspec.options.IntOptions;

import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(IntOptions.class)
public class IntGenerator extends Generator<Integer, IntOptions> {

  private long min;
  private long max;
  private final ThreadLocalRandom random;

  public IntGenerator(IntOptions intOptions) {
    super(intOptions);
    this.min = intOptions.getMin();
    this.max = intOptions.getMax();
    this.random = ThreadLocalRandom.current();
  }

  @Override
  public Integer generate() {
    // (max - min + 1)を加算して、minとmaxの両方を含む範囲を確保
    return (int)(random.nextLong(max - min + 1) + min);
  }
}
