package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.BoolType;

@GenerateOptions(BoolType.class)
public class BoolGenerator extends Generator<Boolean, BoolType> {

  public BoolGenerator(BoolType options) {
    super(options);
  }

  @Override
  public Boolean generate() {
    return Math.random() < type.getTrueRate();
  }
}
