package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.ByteType;
import databullet.domain.generate.GenerateOptions;
import databullet.domain.generate.GenerateStore;

@GenerateOptions(ByteType.class)
public class ByteGenerator extends Generator<String, ByteType> {

  public ByteGenerator(ByteType options) {
    super(options);
  }

  @Override
  public String generate(GenerateStore store) {
    return "TODO";
  }
}
