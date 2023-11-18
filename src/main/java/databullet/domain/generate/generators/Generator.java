package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.DataSpecType;
import databullet.domain.generate.GenerateStore;

public abstract class Generator<T, U extends DataSpecType> {

  protected final U type;

  public Generator(U type) {
    this.type = type;
  }

  public abstract T generate(GenerateStore store);

  public void initialize() {
    // NOP
  }
}