package databullet.domain.generate;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class GenerateRecord {

  @Getter
  private List<Object> data;

  public GenerateRecord() {
    data = new ArrayList<>();
  }

  public void append(Object obj) {
    data.add(obj);
  }

  @Override
  public String toString() {
    return data.toString();
  }
}
