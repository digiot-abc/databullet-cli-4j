package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.ByteType;

@GenerateOptions(ByteType.class)
public class ByteGenerator extends Generator<String, ByteType> {

  public ByteGenerator(ByteType options) {
    super(options);
  }

  @Override
  public String generate() {
    return "TODO";
  }
}
