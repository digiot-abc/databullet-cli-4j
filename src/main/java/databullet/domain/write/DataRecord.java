package databullet.domain.write;

import java.util.ArrayList;
import java.util.List;

public class DataRecord {

  private List<Object> data;

  public DataRecord() {
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
