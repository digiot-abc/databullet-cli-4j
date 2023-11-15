package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.NumericOptions;

@GenerateOptions(NumericOptions.class)
public class NumericGenerator implements Generator<Integer> {

  private long min;
  private long max;

  public NumericGenerator(NumericOptions numericOptions) {
    this.min = numericOptions.getMin();
    this.max = numericOptions.getMax();
  }

  @Override
  public Integer generate() {
    return null;
  }
}
