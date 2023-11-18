package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.DataSpecType;

public abstract class Generator<T, U extends DataSpecType> {

  protected final U type;

  public Generator(U type) {
    this.type = type;
  }

  public abstract T generate();

  public void initialize() {
    // NOP
  }
}