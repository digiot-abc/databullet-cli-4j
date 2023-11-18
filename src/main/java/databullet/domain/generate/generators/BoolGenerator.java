package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.BoolType;
import databullet.domain.generate.GenerateOptions;
import databullet.domain.generate.GenerateStore;

@GenerateOptions(BoolType.class)
public class BoolGenerator extends Generator<Boolean, BoolType> {

  public BoolGenerator(BoolType options) {
    super(options);
  }

  @Override
  public Boolean generate(GenerateStore store) {
    return Math.random() < type.getTrueRate();
  }
}
