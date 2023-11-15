package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.IntOptions;

import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(IntOptions.class)
public class NumericGenerator implements Generator<Integer> {

  private long min;
  private long max;
  private final ThreadLocalRandom random;

  public NumericGenerator(IntOptions intOptions) {
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
